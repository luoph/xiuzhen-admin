package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GameUserAccount;
import cn.youai.xiuzhen.game.service.IGameUserAccountService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.annotation.Readonly;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 账号
 * @date 2023-01-17
 */
@Slf4j
@Readonly
@Api(tags = "账号")
@RestController
@RequestMapping("/game/account")
public class GameUserAccountController extends JeecgController<GameUserAccount, IGameUserAccountService> {

    @AutoLog(value = "账号-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameUserAccount entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "账号-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @GetMapping(value = "/sdkChannels")
    public Result<?> sdkChannels() {
        return Result.ok(service.querySdkChannels());
    }

    @AutoLog(value = "账号-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameUserAccount gameServer) {
        return super.exportXls(request, gameServer, GameUserAccount.class, "账号");
    }

}
