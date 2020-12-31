package org.jeecg.modules.player.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.base.MultiDataSourceController;
import org.jeecg.modules.player.entity.Player;
import org.jeecg.modules.player.service.IPlayerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家信息
 * @date 2019-12-14
 */
@Slf4j
@RestController
@RequestMapping("/player/playerInfo")
public class PlayerInfoController extends MultiDataSourceController<Player, IPlayerService> {

    @Override
    @AutoLog(value = "玩家信息-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Player model,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam(name = "serverId", required = false) Integer serverId,
                                   HttpServletRequest req) {
        if(null == serverId){
            return Result.error("请选择服务器！");
        }
        return super.queryPageList(model, pageNo, pageSize, serverId, req);
    }

//    @Override
//    @AutoLog(value = "玩家信息-添加")
//    @PostMapping(value = "/add")
//    public Result<?> add(@RequestBody Player model, @RequestParam(name = "serverId", required = false) Integer serverId) {
//        return super.add(model, serverId);
//    }

    @Override
    @AutoLog(value = "玩家信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Player model, @RequestParam(name = "serverId", required = false) Integer serverId) {
        return super.edit(model, serverId);
    }

//    @Override
//    @AutoLog(value = "玩家信息-通过id删除")
//    @DeleteMapping(value = "/delete")
//    public Result<?> delete(@RequestParam(name = "id") String id, @RequestParam(name = "serverId", required = false) Integer serverId) {
//        return super.delete(id, serverId);
//    }

//    @Override
//    @AutoLog(value = "玩家信息-批量删除")
//    @DeleteMapping(value = "/deleteBatch")
//    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids, @RequestParam(name = "serverId", required = false) Integer serverId) {
//        return super.deleteBatch(ids, serverId);
//    }

    @Override
    @AutoLog(value = "玩家信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id, @RequestParam(name = "serverId", required = false) Integer serverId) {
        return super.queryById(id, serverId);
    }

    @Override
    @RequestMapping(value = "/exportXls")
    protected ModelAndView exportXls(@RequestParam(name = "serverId", required = false) Integer serverId,
                                     HttpServletRequest request, Player object, Class<Player> clazz, String title) {
        return super.exportXls(serverId, request, object, clazz, title);
    }

    @Override
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    protected Result<?> importExcel(@RequestParam(name = "serverId", required = false) Integer serverId,
                                    HttpServletRequest request, HttpServletResponse response, Class<Player> clazz) {
        return super.importExcel(serverId, request, response, clazz);
    }

}
