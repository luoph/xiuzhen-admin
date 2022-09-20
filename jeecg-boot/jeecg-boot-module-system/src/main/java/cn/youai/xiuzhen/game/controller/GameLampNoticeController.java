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
 * @description game_lamp_notice
 * @date 2020-08-10
 */
@Slf4j
@RestController
@RequestMapping("game/gameLampNotice")
public class GameLampNoticeController extends JeecgController<GameLampNotice, IGameLampNoticeService> {

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @param req      请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_lamp_notice-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameLampNotice entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    /**
     * 添加
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_lamp_notice-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameLampNotice entity) {
        entity.setStatus(1);
        return super.add(entity);
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_lamp_notice-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameLampNotice entity) {
        return super.edit(entity);
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_lamp_notice-通过id删除")
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
    @AutoLog(value = "game_lamp_notice-批量删除")
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
    @AutoLog(value = "game_lamp_notice-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameLampNotice entity) {
        return super.exportXls(request, entity, GameLampNotice.class, "game_lamp_notice");
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
        return super.importExcel(request, response, GameLampNotice.class);
    }

    /**
     * 消息暂停或开启
     *
     * @param id 消息id
     * @return
     */
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
