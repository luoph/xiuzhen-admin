package org.jeecg.modules.player.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.player.entity.GameRegisterInfo;
import org.jeecg.modules.player.service.IGameRegisterInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @Autowired
    private IGameRegisterInfoService playerRegisterInfoService;

    /**
     * 分页列表查询
     *
     * @param registerInfo 数据实体
     * @param pageNo       页码
     * @param pageSize     分页大小
     * @param req          请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家注册信息-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameRegisterInfo registerInfo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameRegisterInfo> queryWrapper = QueryGenerator.initQueryWrapper(registerInfo, req.getParameterMap());
        Page<GameRegisterInfo> page = new Page<>(pageNo, pageSize);
        IPage<GameRegisterInfo> pageList = playerRegisterInfoService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

//    /**
//     * 添加
//     *
//     * @param playerRegisterInfo 数据实体
//     * @return {@linkplain Result}
//     */
//    @AutoLog(value = "玩家注册信息-添加")
//    @PostMapping(value = "/add")
//    public Result<?> add(@RequestBody PlayerRegisterInfo playerRegisterInfo) {
//        playerRegisterInfoService.save(playerRegisterInfo);
//        return Result.ok("添加成功！");
//    }

//    /**
//     * 编辑
//     *
//     * @param playerRegisterInfo 数据实体
//     * @return {@linkplain Result}
//     */
//    @AutoLog(value = "玩家注册信息-编辑")
//    @PutMapping(value = "/edit")
//    public Result<?> edit(@RequestBody PlayerRegisterInfo playerRegisterInfo) {
//        playerRegisterInfoService.updateById(playerRegisterInfo);
//        return Result.ok("编辑成功!");
//    }

//    /**
//     * 通过id删除
//     *
//     * @param id 实体id
//     * @return {@linkplain Result}
//     */
//    @AutoLog(value = "玩家注册信息-通过id删除")
//    @DeleteMapping(value = "/delete")
//    public Result<?> delete(@RequestParam(name = "id") String id) {
//        playerRegisterInfoService.removeById(id);
//        return Result.ok("删除成功!");
//    }

//    /**
//     * 批量删除
//     *
//     * @param ids id列表，使用','分割的字符串
//     * @return {@linkplain Result}
//     */
//    @AutoLog(value = "玩家注册信息-批量删除")
//    @DeleteMapping(value = "/deleteBatch")
//    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
//        this.playerRegisterInfoService.removeByIds(Arrays.asList(ids.split(",")));
//        return Result.ok("批量删除成功！");
//    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家注册信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameRegisterInfo registerInfo = playerRegisterInfoService.getById(id);
        if (registerInfo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(registerInfo);
    }

    /**
     * 导出excel
     *
     * @param request      请求
     * @param registerInfo 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameRegisterInfo registerInfo) {
        return super.exportXls(request, registerInfo, GameRegisterInfo.class, "玩家注册信息");
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
    public Result<?> loginList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                               @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                               @RequestParam(name = "playerId", defaultValue = "0") Long playerId,
                               @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<GameRegisterInfo> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && playerId == 0) {
            return Result.ok(page);
        }
        // 如果选择开始时间和结束时间是同一天
        if (rangeDateBegin.equals(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        List<GameRegisterInfo> list = playerRegisterInfoService.queryLoginList(rangeDateBegin, rangeDateEnd, playerId, serverId);
        page.setRecords(list).setTotal(list.size());
        return Result.ok(page);
    }


}
