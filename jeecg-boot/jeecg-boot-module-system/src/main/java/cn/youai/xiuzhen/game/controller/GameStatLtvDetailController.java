package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GameStatLtvDetail;
import cn.youai.xiuzhen.game.service.IGameStatLtvDetailService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
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
 * @description ltv详细统计
 * @date 2021/12/28
 */
@Slf4j
@RestController
@RequestMapping("/gameStat/ltvDetail")
public class GameStatLtvDetailController extends JeecgController<GameStatLtvDetail, IGameStatLtvDetailService> {

    @Override
    @AutoLog(value = "LTV详细查询")
    @ApiOperation(value = "留存查询", notes = "留存查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameStatLtvDetail entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "LTV详细-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(
            @RequestParam(name = "id") String id) {
        GameStatLtvDetail entity = service.getById(Long.parseLong(id));
        if (entity == null) {
            return Result.error("未找到记录");
        }
        return Result.ok(entity);
    }

    /**
     * 导出excel
     *
     * @param request 请求
     * @param entity  实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameStatLtvDetail entity) {
        return super.exportXls(request, entity, GameStatLtvDetail.class, "LTV详细统计");
    }

}
