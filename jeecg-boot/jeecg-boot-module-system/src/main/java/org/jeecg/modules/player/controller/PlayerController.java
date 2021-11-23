package org.jeecg.modules.player.controller;

import cn.youai.basics.model.DataResponse;
import cn.youai.server.model.RoleAttr;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.xiuzhen.utils.DateUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.base.MultiDataSourceController;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.player.entity.*;
import org.jeecg.modules.player.service.IGameRegisterInfoService;
import org.jeecg.modules.player.service.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家
 * @date 2020-10-19
 */
@Slf4j
@RestController
@RequestMapping("game/player")
public class PlayerController extends MultiDataSourceController<Player, IPlayerService> {

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private IGameServerService gameServerService;

    @Autowired
    private IGameRegisterInfoService playerRegisterInfoService;

    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PlayerDTO playerDTO,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Player> page = new Page<>(pageNo, pageSize);
        if (playerDTO == null) {
            return Result.ok(page);
        }
        List<Player> list = playerService.queryForList(playerDTO);
        if (list == null) {
            list = new ArrayList<>();
        }
        page.setRecords(list).setTotal(list.size());
        return Result.ok(page);
    }

    /**
     * 分页列表查询
     *
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家详情-查询")
    @GetMapping(value = "/detail")
    public Result<?> queryDetail(@RequestParam(name = "playerId", defaultValue = "0") Integer playerId) {
        LambdaQueryWrapper<GameRegisterInfo> queryWrapper = Wrappers.<GameRegisterInfo>lambdaQuery().eq(GameRegisterInfo::getPlayerId, playerId);
        GameRegisterInfo registerInfo = playerRegisterInfoService.getOne(queryWrapper);
        if (null == registerInfo) {
            return Result.error("玩家信息不存在");
        }
        int serverId = registerInfo.getServerId();
        GameServer gameServer = gameServerService.getById(serverId);
        // http调用查询玩家详情
        String str = OkHttpHelper.get(gameServer.getGmUrl() + "/player/info?playerId=" + playerId);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(JSON.parseObject(str, DataResponse.class).getData()));
        String roleAttrStr = jsonObject.getString("roleAttr");
        RoleAttr roleAttr = JSONObject.parseObject(roleAttrStr, RoleAttr.class);
        return Result.ok(roleAttr);
    }


    /**
     * 分页列表查询
     *
     * @return {@linkplain Result}
     */
    @AutoLog(value = "玩家行为分析-查询")
    @GetMapping(value = "/behavior")
    public Result<?> queryBehavior(PlayerBehaviorVO playerBehaviorVO,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {

        //服务器校验
        if (playerBehaviorVO.getServerId() == null || playerBehaviorVO.getServerId() <= 0) {
            return Result.error("请选择服务器！");
        }

        //日期空校验
        if (playerBehaviorVO.getDaysType() == null || playerBehaviorVO.getDaysType() <= 0) {
            if (playerBehaviorVO.getRangeDateBegin() == null || playerBehaviorVO.getRangeDateEnd() == null) {
                return Result.error("请选择日期！");
            }
            if (playerBehaviorVO.getRangeDateBegin() != null && playerBehaviorVO.getRangeDateEnd() != null) {
                // 如果选择开始时间和结束时间是同一天
                Date[] dates = DateUtils.queryDateOfSameDay(playerBehaviorVO.getRangeDateBegin(), playerBehaviorVO.getRangeDateEnd());
                playerBehaviorVO.setRangeDateBegin(dates[0]);
                playerBehaviorVO.setRangeDateEnd(dates[1]);
            }
        }

        Page<PlayerBehavior> page = new Page<>(pageNo, pageSize);
        List<PlayerBehavior> list = playerService.queryPlayerBehavior(playerBehaviorVO.getRangeDateBegin(), playerBehaviorVO.getRangeDateEnd(),
                playerBehaviorVO.getNickname(), playerBehaviorVO.getPlayerId(), playerBehaviorVO.getDaysType() == null ? 0 : playerBehaviorVO.getDaysType(), playerBehaviorVO.getServerId());
        page.setRecords(list).setTotal(list.size());
        return Result.ok(page);
    }

    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(PlayerBehaviorVO playerBehaviorVO,
                                  HttpServletRequest request) {

        //服务器校验
        if (playerBehaviorVO.getServerId() == null || playerBehaviorVO.getServerId() <= 0) {
            return new ModelAndView();
        }

        //日期空校验
        if (playerBehaviorVO.getDaysType() == null || playerBehaviorVO.getDaysType() <= 0) {
            if (playerBehaviorVO.getRangeDateBegin() == null || playerBehaviorVO.getRangeDateEnd() == null) {
                return new ModelAndView();
            }
            if (playerBehaviorVO.getRangeDateBegin() != null && playerBehaviorVO.getRangeDateEnd() != null) {
                // 如果选择开始时间和结束时间是同一天
                Date[] dates = DateUtils.queryDateOfSameDay(playerBehaviorVO.getRangeDateBegin(), playerBehaviorVO.getRangeDateEnd());
                playerBehaviorVO.setRangeDateBegin(dates[0]);
                playerBehaviorVO.setRangeDateEnd(dates[1]);
            }
        }

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PlayerBehavior> list = playerService.queryPlayerBehavior(playerBehaviorVO.getRangeDateBegin(), playerBehaviorVO.getRangeDateEnd(),
                playerBehaviorVO.getNickname(), playerBehaviorVO.getPlayerId(), playerBehaviorVO.getDaysType() == null ? 0 : playerBehaviorVO.getDaysType(), playerBehaviorVO.getServerId());
        return ExcelUtils.exportXls(sysUser.getRealname(), list, request.getParameter("selections"), PlayerBehavior.class, "玩家行为分析");
    }

    @RequestMapping("/downloadPlayerInfo")
    public void downloadPlayerInfo(HttpServletResponse response, @RequestBody JSONObject jsonObject) throws Exception {
        PlayerDTO playerDTO = JSON.parseObject(jsonObject.toJSONString(), PlayerDTO.class);
        List<Player> list = playerService.queryForList(playerDTO);
        if (list == null) {
            list = new ArrayList<>();
        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("excel导出文件名", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), Player.class).sheet("模板").doWrite(list);
    }

}
















