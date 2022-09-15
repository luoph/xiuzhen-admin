package cn.youai.xiuzhen.stat.controller;

import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import cn.youai.xiuzhen.stat.service.IGameOrderStatService;
import cn.youai.xiuzhen.utils.QueryUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Map;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
@Slf4j
@RestController
@RequestMapping("game/order")
public class GameOrderController extends JeecgController<GameOrder, IGameOrderStatService> {

    @Autowired
    private IGamePlayerService playerService;

    /**
     * 分页列表查询
     *
     * @return {@linkplain Result}
     */
    @AutoLog(value = "充值订单-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameOrder entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<GameOrder> pageList = pageList(entity, pageNo, pageSize, req);
        HashSet<Long> resultPlayerIds = QueryUtils.extractPlayerIds(pageList.getRecords(), GameOrder::getPlayerId);
        Map<Long, String> nicknameMap = playerService.getPlayerNicknameMap(resultPlayerIds);
        pageList.getRecords().forEach(e -> e.setNickname(nicknameMap.get(e.getPlayerId())));
        return Result.ok(pageList);
    }

    /**
     * 通过id查询
     */
    @AutoLog(value = "充值订单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    /**
     * 添加
     */
    @AutoLog(value = "充值订单-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameOrder entity) {
        return Result.ok("不支持！");
    }

    @Override
    public Result<?> delete(String id) {
        return Result.ok("不支持！");
    }

    @Override
    public Result<?> deleteBatch(String ids) {
        return Result.ok("不支持！");
    }

    @AutoLog(value = "充值订单-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameOrder entity) {
        IPage<GameOrder> pageList = pageList(entity, 1, Integer.MAX_VALUE, request);
        HashSet<Long> resultPlayerIds = QueryUtils.extractPlayerIds(pageList.getRecords(), GameOrder::getPlayerId);
        Map<Long, String> nicknameMap = playerService.getPlayerNicknameMap(resultPlayerIds);
        pageList.getRecords().forEach(e -> e.setNickname(nicknameMap.get(e.getPlayerId())));
        return ExcelUtils.exportXls(pageList, request.getParameter("selections"), GameOrder.class, "充值订单");
    }
}
