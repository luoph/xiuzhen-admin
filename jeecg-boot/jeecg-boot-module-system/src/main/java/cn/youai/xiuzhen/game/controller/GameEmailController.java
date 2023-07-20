package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.lock.ChainLock;
import cn.youai.basics.lock.LockUtils;
import cn.youai.basics.model.Response;
import cn.youai.basics.utils.StringUtils;
import cn.youai.server.conf.ConfItem;
import cn.youai.server.model.ItemVO;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameEmail;
import cn.youai.xiuzhen.game.service.IGameEmailService;
import cn.youai.xiuzhen.utils.RewardUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.utils.GameConfigUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @AutoLog(value = "游戏邮件-列表查询")
    @GetMapping(value = "/list")
    @PermissionData(value = "game/GameEmailList")
    public Result<?> queryPageList(GameEmail entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    private void mergeItem(GameEmail entity) {
        String content = entity.getContent();
        if (StringUtils.isBlank(content)) {
            return;
        }

        List<ItemVO> itemList = JSON.parseArray(content, ItemVO.class);
        entity.setContent(JSON.toJSONString(RewardUtils.merge(itemList)));
    }

    @AutoLog(value = "游戏邮件-添加")
    @PostMapping(value = "/add")
    @PermissionData(value = "game/GameChannelList")
    public Result<?> add(@RequestBody GameEmail entity, HttpServletRequest req) {
        entity.setState(0).setReviewBy(null).setReviewTime(null).sortReceiverIds();
        List<Long> receiverIds = StringUtils.split2Long(entity.getReceiverIds());
        if (CollUtil.isEmpty(receiverIds)) {
            return Result.error("目标主体未设置！");
        }

        try {
            mergeItem(entity);
        } catch (Exception e) {
            log.error("mergeItem error, entity:" + entity, e);
            return Result.error("附格式错误！");
        }

        Response response = service.saveEmail(entity, JwtUtil.getUserNameByToken(req));
        if (!response.isSuccess()) {
            return Result.error(response.getDesc());
        }
        return Result.ok("添加成功！");
    }

    @AutoLog(value = "游戏邮件-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameEmail entity, HttpServletRequest req) {
        if (entity.getState() != null && entity.getState() == 1) {
            return Result.error("邮件已发送！无法编辑！");
        }

        try {
            mergeItem(entity);
        } catch (Exception e) {
            log.error("mergeItem error, entity:" + entity, e);
            return Result.error("附件格式错误！");
        }
        return super.edit(entity);
    }

    @AutoLog(value = "游戏邮件-通过id删除")
    @DeleteMapping(value = "/delete")
    @RequiresPermissions("game:email:review")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "游戏邮件-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "游戏邮件-导出")
    @RequestMapping(value = "/exportXls")
    @PermissionData(value = "game/GameEmailList")
    @RequiresPermissions("game:email:review")
    public ModelAndView exportXls(HttpServletRequest request, GameEmail gameEmail) {
        return super.exportXls(request, gameEmail, GameEmail.class, "游戏邮件");
    }

    @AutoLog(value = "游戏邮件-导入")
    @RequiresPermissions("game:email:review")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameEmail.class);
    }

    /**
     * 获取道具树
     */
    @RequestMapping(value = "/itemTree", method = RequestMethod.GET)
    public Result<?> itemTree(@RequestParam(name = "itemId", required = false) String itemId,
                              @RequestParam(name = "itemName", required = false) String itemName) {
        List<Integer> itemIdList = null;
        if (StringUtils.isNotBlank(itemId)) {
            String[] split = itemId.contains(StringUtils.SEPARATOR_COMMA) ? itemId.split(StringUtils.SEPARATOR_COMMA)
                    : itemId.contains(" ") ? itemId.split(" ")
                    : new String[]{itemId};
            itemIdList = Arrays.stream(split).filter(StringUtils::isNotBlank).map(e -> Integer.valueOf(e.trim())).collect(Collectors.toList());
        }

        List<String> itemNameList = null;
        if (StringUtils.isNotBlank(itemName)) {
            String[] split = itemName.contains(StringUtils.SEPARATOR_COMMA) ? itemName.split(StringUtils.SEPARATOR_COMMA)
                    : itemName.contains(" ") ? itemName.split(" ")
                    : new String[]{itemName};
            itemNameList = Arrays.stream(split).filter(StringUtils::isNotBlank).map(String::trim).collect(Collectors.toList());
        }

        List<ConfItem> items = GameConfigUtils.getConfItemList(itemIdList, itemNameList);
        return Result.ok(items);
    }

    @AutoLog(value = "游戏邮件-审核")
    @RequestMapping(value = "/review", method = RequestMethod.GET)
    @RequiresPermissions("game:email:review")
    public Result<?> review(@RequestParam("id") long id, HttpServletRequest req) {
        GameEmail entity = service.getById(id);
        if (entity == null) {
            return Result.error("邮件不存在！");
        }

        if (entity.getState() == 1) {
            return Result.error("邮件已发送！");
        }

        ChainLock lock = LockUtils.getLock(getClass().getSimpleName(), "review", id);
        try {
            lock.lock();
            Response response = service.sendEmail(entity, JwtUtil.getUserNameByToken(req));
            if (response.isSuccess()) {
                entity.setState(1);
                String username = JwtUtil.getUserNameByToken(req);
                entity.setReviewBy(username).setReviewTime(DateUtils.now());
                service.updateById(entity);
                return Result.ok("发送成功！");
            }
        } finally {
            lock.unlock();
        }
        return Result.error("审核失败");
    }

}
