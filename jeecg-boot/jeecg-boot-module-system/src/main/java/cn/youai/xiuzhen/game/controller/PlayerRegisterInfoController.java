package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GameRegisterInfo;
import cn.youai.xiuzhen.game.service.IGameRegisterInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.annotation.Readonly;
import org.jeecg.common.system.base.controller.JeecgController;
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
@Readonly
@RestController
@RequestMapping("player/registerInfo")
public class PlayerRegisterInfoController extends JeecgController<GameRegisterInfo, IGameRegisterInfoService> {

    @AutoLog(value = "玩家注册信息-列表查询")
    @GetMapping(value = "/list")
    @PermissionData(value = "player/GameRegisterInfoList")
    public Result<?> queryPageList(GameRegisterInfo entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "玩家注册信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "玩家注册信息-导出")
    @RequestMapping(value = "/exportXls")
    @PermissionData(value = "player/GameRegisterInfoList")
    public ModelAndView exportXls(HttpServletRequest request, GameRegisterInfo entity) {
        return super.exportXls(request, entity, GameRegisterInfo.class, "玩家注册信息");
    }

    @AutoLog(value = "登录流水-列表查询")
    @GetMapping(value = "/loginList")
    @PermissionData(value = "player/GameRegisterInfoList")
    public Result<?> loginList(@RequestParam(name = "startDate", defaultValue = "") String startDate,
                               @RequestParam(name = "endDate", defaultValue = "") String endDate,
                               @RequestParam(name = "playerId", defaultValue = "0") Long playerId,
                               @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<GameRegisterInfo> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate) && serverId == 0 && playerId == 0) {
            return Result.ok(page);
        }
        List<GameRegisterInfo> list = service.queryLoginList(serverId, playerId, startDate, endDate);
        page.setRecords(list).setTotal(list.size());
        return Result.ok(page);
    }
}
