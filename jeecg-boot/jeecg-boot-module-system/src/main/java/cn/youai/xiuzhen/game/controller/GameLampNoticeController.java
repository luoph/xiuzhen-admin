package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GameLampNotice;
import cn.youai.xiuzhen.game.service.IGameLampNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 跑马灯消息
 * @date 2020-08-10
 */
@Slf4j
@RestController
@RequestMapping("game/gameLampNotice")
public class GameLampNoticeController extends JeecgController<GameLampNotice, IGameLampNoticeService> {

    @AutoLog(value = "跑马灯消息-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameLampNotice entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "跑马灯消息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameLampNotice entity) {
        entity.setStatus(1);
        return super.add(entity);
    }

    @AutoLog(value = "跑马灯消息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameLampNotice entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "跑马灯消息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "跑马灯消息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "跑马灯消息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "跑马灯消息-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameLampNotice entity) {
        return super.exportXls(request, entity, GameLampNotice.class, "跑马灯消息");
    }

    @AutoLog(value = "跑马灯消息-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameLampNotice.class);
    }

    @AutoLog(value = "跑马灯消息-暂停或开启")
    @GetMapping(value = "/pauseOrOpen")
    public Result<Object> pauseLampNotice(@RequestParam(name = "id") int id) {
        GameLampNotice gameLampNotice = service.getById(id);
        if (gameLampNotice == null) {
            return Result.error("跑马灯消息不存在！");
        }
        // 消息状态1-开启 0-暂定
        int status = gameLampNotice.getStatus();
        if (status == 1) {
            gameLampNotice.setStatus(0);
        } else {
            gameLampNotice.setStatus(1);
        }
        service.updateById(gameLampNotice);
        return Result.ok("消息状态更新成功！");
    }
}
