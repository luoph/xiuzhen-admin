package cn.youai.xiuzhen.game.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.server.exception.AppException;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.core.controller.SimplePageController;
import cn.youai.xiuzhen.game.entity.GameStopServerRefundRecord;
import cn.youai.xiuzhen.game.mapper.GameStopServerRefundRecordMapper;
import cn.youai.xiuzhen.game.service.IGameStopServerRefundRecordService;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @Description: 删档返还
 * @Author: jeecg-boot
 * @Date: 2022-12-14
 * @Version: V1.0
 */
@Api(tags = "删档返还")
@RestController
@RequestMapping("/game/gameStopServerRefundRecord")
@Slf4j
public class GameStopServerRefundRecordController extends SimplePageController<GameStopServerRefundRecord> {
    @Autowired
    private IGameStopServerRefundRecordService gameStopServerRefundRecordService;
    @Resource
    private GameStopServerRefundRecordMapper gameStopServerRefundRecordMapper;

    /**
     * 分页列表查询
     *
     * @param entity
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "删档返还-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameStopServerRefundRecord entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @Override
    protected IPage<GameStopServerRefundRecord> pageList(Page<GameStopServerRefundRecord> page, GameStopServerRefundRecord entity, HttpServletRequest req) throws AppException {
        RangeValue<BigDecimal> sourceAmountRange = PageQueryUtils.parseNumberRange(req.getParameterMap(), "sourceAmount");
        RangeValue<BigDecimal> targetNumRange = PageQueryUtils.parseNumberRange(req.getParameterMap(), "targetNum");
        DateRange createTimeRange = PageQueryUtils.parseRange(req.getParameterMap(), "createTime");
        return gameStopServerRefundRecordMapper.pageList(page, entity, sourceAmountRange, targetNumRange, createTimeRange);
    }

    /**
     * 添加
     *
     * @param gameStopServerRefundRecord
     * @return
     */
    @AutoLog(value = "删档返还-添加")
    //@RequiresPermissions("cn.youai.xiuzhen:game_stop_server_refund_record:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody GameStopServerRefundRecord gameStopServerRefundRecord) {
        gameStopServerRefundRecordService.save(gameStopServerRefundRecord);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameStopServerRefundRecord
     * @return
     */
    @AutoLog(value = "删档返还-编辑")
    //@RequiresPermissions("cn.youai.xiuzhen:game_stop_server_refund_record:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody GameStopServerRefundRecord gameStopServerRefundRecord) {
        gameStopServerRefundRecordService.updateById(gameStopServerRefundRecord);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删档返还-通过id删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_stop_server_refund_record:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        gameStopServerRefundRecordService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "删档返还-批量删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_stop_server_refund_record:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gameStopServerRefundRecordService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "删档返还-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<GameStopServerRefundRecord> queryById(@RequestParam(name = "id", required = true) String id) {
        GameStopServerRefundRecord gameStopServerRefundRecord = gameStopServerRefundRecordService.getById(id);
        if (gameStopServerRefundRecord == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gameStopServerRefundRecord);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gameStopServerRefundRecord
     */
    //@RequiresPermissions("cn.youai.xiuzhen:game_stop_server_refund_record:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameStopServerRefundRecord gameStopServerRefundRecord) {
        return super.exportXls(request, gameStopServerRefundRecord, GameStopServerRefundRecord.class, "删档返还");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    //@RequiresPermissions("game_stop_server_refund_record:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return Result.error("不支持");
    }

}
