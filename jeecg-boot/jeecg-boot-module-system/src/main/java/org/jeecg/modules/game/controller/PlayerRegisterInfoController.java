package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.game.service.IGameRegisterInfoService;
import org.jeecg.modules.game.entity.GameRegisterInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家注册信息
 * @date 2020-01-04
 */
@Slf4j
@RestController
@RequestMapping("player/playerRegisterInfo")
public class PlayerRegisterInfoController extends JeecgController<GameRegisterInfo, IGameRegisterInfoService> {

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @param req      请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家注册信息-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameRegisterInfo entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    /**
     * 添加
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家注册信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameRegisterInfo entity) {
        return Result.ok("不支持！");
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家注册信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameRegisterInfo entity) {
        return Result.ok("不支持！");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家注册信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return Result.ok("不支持！");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家注册信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return Result.ok("不支持！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家注册信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    /**
     * 导出excel
     *
     * @param request 请求
     * @param entity  实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameRegisterInfo entity) {
        return super.exportXls(request, entity, GameRegisterInfo.class, "玩家注册信息");
    }

    //    /**
//     * 通过excel导入数据
//     *
//     * @param request  请求
//     * @param response 响应
//     * @return {@linkplain Result}
//     */
//    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
//    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
//        return super.importExcel(request, response, PlayerRegisterInfo.class);
//    }

    @AutoLog(value = "登录流水-列表查询")
    @GetMapping(value = "/loginList")
    public Result<?> loginList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin, @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd, @RequestParam(name = "playerId", defaultValue = "0") Long playerId, @RequestParam(name = "serverId", defaultValue = "0") Integer serverId, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<GameRegisterInfo> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && playerId == 0) {
            return Result.ok(page);
        }
        // 如果选择开始时间和结束时间是同一天
        if (rangeDateBegin.equals(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        List<GameRegisterInfo> list = service.queryLoginList(rangeDateBegin, rangeDateEnd, playerId, serverId);
        page.setRecords(list).setTotal(list.size());
        return Result.ok(page);
    }


}
