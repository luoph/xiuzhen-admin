package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.util.StrUtil;
import cn.youai.xiuzhen.game.entity.GameUpgradeNotice;
import cn.youai.xiuzhen.game.service.IGameUpgradeNoticeService;
import cn.youai.xiuzhen.utils.StrHtmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 更新公告
 * @date 2021-03-02
 */
@Slf4j
@RestController
@RequestMapping("game/gameUpgradeNotice")
public class GameUpgradeNoticeController extends JeecgController<GameUpgradeNotice, IGameUpgradeNoticeService> {

    @AutoLog(value = "更新公告-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameUpgradeNotice entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "更新公告-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameUpgradeNotice entity) {
        entity.setNoticeMsg(StrHtmlUtils.formatNoticeHtml(entity.getNoticeMsg()));
        // 排序区服id
        List<String> serverIds = StrUtil.splitTrim(entity.getServerIds() != null ? entity.getServerIds() : "", ",");
        Collections.sort(serverIds);
        entity.setServerIds(StrUtil.join(",", serverIds));
        return super.add(entity);
    }

    @AutoLog(value = "更新公告-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameUpgradeNotice entity) {
        entity.setNoticeMsg(StrHtmlUtils.formatNoticeHtml(entity.getNoticeMsg()));
        // 排序区服id
        List<String> serverIds = StrUtil.splitTrim(entity.getServerIds() != null ? entity.getServerIds() : "", ",");
        Collections.sort(serverIds);
        String newServerIds = StrUtil.join(",", serverIds);
        if (StringUtils.equals(newServerIds, entity.getServerIds())) {
            entity.setServerIds(newServerIds);
        }
        return super.edit(entity);
    }

    @AutoLog(value = "更新公告-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "更新公告-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "更新公告-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "更新公告-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameUpgradeNotice entity) {
        return super.exportXls(request, entity, GameUpgradeNotice.class, "更新公告");
    }

    @AutoLog(value = "更新公告-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameUpgradeNotice.class, "更新公告");
    }

    @AutoLog(value = "更新公告-开关")
    @GetMapping(value = "/openOrClose")
    public Result<?> openOrClose(@RequestParam(name = "id") int id) {
        GameUpgradeNotice gameUpgradeNotice = service.getById(id);
        if (gameUpgradeNotice == null) {
            return Result.error("消息不存在！");
        }
        // 消息状态1-开启 0-暂定
        int status = gameUpgradeNotice.getStatus();
        if (status == 1) {
            gameUpgradeNotice.setStatus(0);
        } else {
            gameUpgradeNotice.setStatus(1);
        }
        service.updateById(gameUpgradeNotice);
        return Result.ok("更新成功！");
    }


    @AutoLog(value = "更新公告-同步到游戏服")
    @GetMapping(value = "/serverSync")
    public Result<?> serverSync(@RequestParam(name = "id") int id) {
        GameUpgradeNotice gameUpgradeNotice = service.getById(id);
        if (gameUpgradeNotice == null) {
            return Result.error("消息不存在！");
        }

        if (StringUtils.isBlank(gameUpgradeNotice.getServerIds())) {
            return Result.error("请配置活动游戏服务器！");
        }
        service.syncServerAll(gameUpgradeNotice);
        return Result.ok("同步成功！");
    }
}
