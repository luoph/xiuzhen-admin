package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.utils.StringUtils;
import cn.youai.server.conf.ConfItem;
import cn.youai.xiuzhen.game.entity.GameEmail;
import cn.youai.xiuzhen.game.service.IGameEmailService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.utils.GameConfigUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏邮件
 * @date 2020-01-04
 */
@Slf4j
@RestController
@RequestMapping("game/gameEmail")
public class GameEmailController extends JeecgController<GameEmail, IGameEmailService> {

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @param req      请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏邮件-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameEmail entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    /**
     * 添加
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏邮件-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameEmail entity) {
        log.info("gameEmail:{}", JSON.toJSONString(entity));
        List<Long> receiverIds = StringUtils.split2Long(entity.getReceiverIds());
        if (CollUtil.isEmpty(receiverIds)) {
            return Result.error("所选投放对象不允许为空！");
        }
        service.saveEmail(entity);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏邮件-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameEmail entity) {
        return super.edit(entity);
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏邮件-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏邮件-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏邮件-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    /**
     * 导出excel
     *
     * @param request   请求
     * @param gameEmail 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameEmail gameEmail) {
        return super.exportXls(request, gameEmail, GameEmail.class, "游戏邮件");
    }

    /**
     * 通过excel导入数据
     *
     * @param request  请求
     * @param response 响应
     * @return {@linkplain Result}
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameEmail.class);
    }

    /**
     * 获取道具树
     */
    @RequestMapping(value = "/itemTree", method = RequestMethod.GET)
    public Result<?> itemTree(@RequestParam(name = "itemId", required = false) Integer itemId,
                              @RequestParam(name = "itemName", required = false) String itemName) {
        if (itemName != null) {
            if ("".equals(itemName)) {
                itemName = null;
            }
        }
        List<ConfItem> items = GameConfigUtils.getConfItemList(itemId, itemName);
        return Result.ok(items);
    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public Result<?> isCheck(@RequestParam("id") long id) {
        GameEmail entity = service.getById(id);
        if (entity == null) {
            return Result.error("邮件不存在！");
        }
        if (entity.getState() == 1) {
            return Result.error("已审核发送！");
        }
        entity.setState(1);
        service.updateById(new GameEmail().setId(entity.getId()).setState(1));
        Response response = service.sendEmail(entity);
        if (!response.isSuccess()) {
            return Result.error(response.getDesc());
        }
        return Result.ok("审核发送成功！");
    }
}
