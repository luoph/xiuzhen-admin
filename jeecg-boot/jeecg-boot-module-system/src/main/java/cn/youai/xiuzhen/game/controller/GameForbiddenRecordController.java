package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GameForbiddenRecord;
import cn.youai.xiuzhen.game.service.IGameForbiddenRecordService;
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
 * @description 封禁记录
 * @date 2021-01-23
 */
@Slf4j
@Readonly
@RestController
@RequestMapping("game/forbiddenRecord")
public class GameForbiddenRecordController extends JeecgController<GameForbiddenRecord, IGameForbiddenRecordService> {

    @AutoLog(value = "封禁记录-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameForbiddenRecord entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "封禁记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "封禁记录-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameForbiddenRecord entity) {
        return super.exportXls(request, entity, GameForbiddenRecord.class, "封禁记录");
    }
}
