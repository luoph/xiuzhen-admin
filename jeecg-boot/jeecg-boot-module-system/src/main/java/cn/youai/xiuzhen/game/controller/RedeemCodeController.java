package cn.youai.xiuzhen.game.controller;

import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.game.entity.GameRedeemCode;
import cn.youai.xiuzhen.game.service.IGameRedeemCodeService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.RandomStringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 激活码
 * @date 2020-01-07
 */
@Slf4j
@RestController
@RequestMapping("game/redeemCode")
public class RedeemCodeController extends JeecgController<GameRedeemCode, IGameRedeemCodeService> {

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @param req      请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "激活码-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameRedeemCode entity,
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
    @AutoLog(value = "激活码-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameRedeemCode entity) {
        if (entity.getBatchNum() != null && entity.getBatchNum() > 0) {
            // 1.批量自动生成激活码
            List<GameRedeemCode> gameRedeemCodes = new ArrayList<>(entity.getBatchNum());
            for (int i = 0; i < entity.getBatchNum(); i++) {
                // 自动生产激活码
                String code = RandomStringUtils.genCdKeyCode();
                gameRedeemCodes.add(new GameRedeemCode(entity.getActivityId(), code, entity.getTotalNum(), 0, entity.getStatus()));
            }
            service.saveBatch(gameRedeemCodes, gameRedeemCodes.size());
        } else if (entity.getCode() != null && entity.getCode().contains(",")) {
            // 2.批量激活码字符串生成
            Set<String> codes = StringUtils.split2Set(entity.getCode());
            List<GameRedeemCode> gameRedeemCodes = new ArrayList<>(codes.size());
            for (String code : codes) {
                // 自动生产激活码
                gameRedeemCodes.add(new GameRedeemCode(entity.getActivityId(), code, entity.getTotalNum(), 0, entity.getStatus()));
            }
            service.saveBatch(gameRedeemCodes, gameRedeemCodes.size());
        } else {
            // 3.单个添加
            service.save(entity);
        }
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "激活码-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameRedeemCode entity) {
        return super.edit(entity);
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "激活码-通过id删除")
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
    @AutoLog(value = "激活码-批量删除")
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
    @AutoLog(value = "激活码-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameRedeemCode entity) {
        return super.exportXls(request, entity, GameRedeemCode.class, "激活码");
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
        return super.importExcel(request, response, GameRedeemCode.class);
    }

}
