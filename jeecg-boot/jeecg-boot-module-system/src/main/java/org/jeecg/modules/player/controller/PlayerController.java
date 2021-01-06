package org.jeecg.modules.player.controller;

import cn.hutool.core.date.DatePattern;
import cn.youai.commons.model.DataResponse;
import cn.youai.xiuzhen.entity.pojo.RoleAttr;
import cn.youai.xiuzhen.utils.DateUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.okhttp.OkHttpHelper;
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
import java.io.IOException;
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
    public Result<?> queryDetai(@RequestParam(name = "playerId", defaultValue = "0") Integer playerId) {
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
    public Result<?> queryBehavior(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                   @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                   @RequestParam(name = "nickname", defaultValue = "") String nickname,
                                   @RequestParam(name = "days", defaultValue = "0") int days,
                                   @RequestParam(name = "serverId", defaultValue = "0") int serverId,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        if (StringUtils.isAllBlank(rangeDateBegin, rangeDateEnd)) {
            return Result.error("");
        }
        Page<PlayerBehavior> page = new Page<>(pageNo, pageSize);
        // 如果选择开始时间和结束时间是同一天
        if (rangeDateBegin.equals(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        List<PlayerBehavior> list = playerService.queryPlayerBehavior(rangeDateBegin, rangeDateEnd, nickname, days, serverId);

        page.setRecords(list).setTotal(list.size());
        return Result.ok(page);
    }

    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(
            @RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
            @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
            @RequestParam(name = "nickname", defaultValue = "") String nickname,
            @RequestParam(name = "days", defaultValue = "0") int days,
            @RequestParam(name = "serverId", defaultValue = "0") int serverId,
            HttpServletRequest request) {

        if (StringUtils.isAllBlank(rangeDateBegin, rangeDateEnd)) {
            return new ModelAndView();
        }

        // TODO 复用博栋的写法 后面统一改
        if (rangeDateBegin.equals(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PlayerBehavior> list = playerService.queryPlayerBehavior(rangeDateBegin, rangeDateEnd, nickname, days, serverId);
        return ExcelUtils.exportXls(sysUser.getRealname(), list, request.getParameter("selections"), PlayerBehavior.class, "玩家行为分析");
    }

    @RequestMapping("/download")
        public void download(HttpServletResponse response, @RequestBody JSONObject jsonObject) throws Exception {
            String nickname = null == jsonObject.getString("nickname") ? "":jsonObject.getString("nickname");
            String rangeDateBegin = null == jsonObject.getString("rangeDateBegin") ? "":jsonObject.getString("rangeDateBegin");;
            String rangeDateEnd = null == jsonObject.getString("rangeDateEnd") ? "":jsonObject.getString("rangeDateEnd");;
            Integer serverId =  null == jsonObject.getString("serverId") ? 0:Integer.parseInt(jsonObject.getString("serverId"));
            Integer days =  null == jsonObject.getString("days") ? 0:Integer.parseInt(jsonObject.getString("days"));

        //日期空校验
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if(0 == days){
                throw new Exception("导出文件，请选择日期！");
            } else {
                rangeDateEnd = DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN) + " 23:59:59";;
                rangeDateBegin = DateUtils.formatDate(DateUtils.addDays(new Date(), days * (-1) + 1), DatePattern.NORM_DATE_PATTERN) + " 00:00:00";;
            }
        }

        // TODO 复用博栋的写法 后面统一改
        if (rangeDateBegin.equals(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }


        List<PlayerBehavior> list = playerService.queryPlayerBehavior(rangeDateBegin, rangeDateEnd, nickname, days, serverId);

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("测试", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            EasyExcel.write(response.getOutputStream(), PlayerBehavior.class).sheet("模板").doWrite(list);
        }

}
















