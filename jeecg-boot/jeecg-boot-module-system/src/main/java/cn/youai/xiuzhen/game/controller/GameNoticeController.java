package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GameNotice;
import cn.youai.xiuzhen.game.model.NoticeConfig;
import cn.youai.xiuzhen.game.service.IGameNoticeService;
import cn.youai.xiuzhen.utils.StrHtmlUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.JsonFileUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏公告
 * @date 2019-12-13
 */
@Slf4j
@RestController
@Api(tags = "游戏公告")
@RequestMapping("/game/gameNotice")
public class GameNoticeController extends JeecgController<GameNotice, IGameNoticeService> {

    @Value("${app.folder.notice}")
    private String noticeFolder;

    @AutoLog(value = "游戏公告-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameNotice entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "游戏公告-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameNotice entity) {
        entity.setContent(StrHtmlUtils.formatNoticeHtml(entity.getContent()));
        return super.add(entity);
    }

    @AutoLog(value = "游戏公告-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameNotice entity) {
        entity.setContent(StrHtmlUtils.formatNoticeHtml(entity.getContent()));
        return super.edit(entity);
    }

    @AutoLog(value = "游戏公告-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "游戏公告-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "游戏公告-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "游戏公告-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameNotice entity) {
        return super.exportXls(request, entity, GameNotice.class, "游戏公告");
    }

    @AutoLog(value = "游戏公告-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameNotice.class);
    }

    @AutoLog(value = "游戏公告-刷新配置")
    @GetMapping(value = "/updateNoticeConfig")
    public Result<?> updateNoticeConfig(HttpServletRequest req) {
        try {
            List<GameNotice> noticeList = service.list();
            for (GameNotice gameNotice : noticeList) {
                updateNoticeJson(gameNotice);
            }
        } catch (Exception e) {
            log.error("updateNoticeConfig error", e);
            return Result.error(e.getMessage());
        }
        return Result.ok("刷新成功");
    }

    @AutoLog(value = "游戏公告-通过id刷新公告")
    @GetMapping(value = "/refreshById")
    public Result<?> refreshById(@RequestParam(name = "id") String id) {
        GameNotice gameNotice = service.getById(id);
        if (gameNotice == null) {
            return Result.error("找不到对应的公告");
        }
        updateNoticeJson(gameNotice);
        return Result.ok("刷新成功");
    }

    private void updateNoticeJson(GameNotice gameNotice) {
        NoticeConfig notice = new NoticeConfig();
        BeanUtils.copyProperties(gameNotice, notice);
        if (gameNotice.getStatus() == 1) {
            JsonFileUtils.writeJsonFile(notice, noticeFolder, String.valueOf(gameNotice.getId()));
        } else {
            JsonFileUtils.deleteJsonFile(noticeFolder, String.valueOf(gameNotice.getId()));
        }
    }
}
