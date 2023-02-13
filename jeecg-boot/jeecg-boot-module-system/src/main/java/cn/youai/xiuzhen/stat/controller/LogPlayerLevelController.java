package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.core.controller.SimplePageController;
import cn.youai.xiuzhen.stat.entity.LogPlayerLevel;
import cn.youai.xiuzhen.stat.service.ILogPlayerLevelService;
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

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: 境界日志
 * @Author: jeecg-boot
 * @Date: 2023-02-10
 * @Version: V1.0
 */
@Api(tags = "境界日志")
@RestController
@RequestMapping("/stat/logPlayerLevel")
@Slf4j
public class LogPlayerLevelController extends SimplePageController<LogPlayerLevel> {

    @Autowired
    private ILogPlayerLevelService logPlayerLevelService;

    @GetMapping(value = "/list")
    public Result<?> queryPageList(LogPlayerLevel entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
//        QueryWrapper<LogPlayerLevel> queryWrapper = QueryGenerator.initQueryWrapper(entity, req.getParameterMap());
//        Page<LogPlayerLevel> page = new Page<LogPlayerLevel>(pageNo, pageSize);
//        IPage<LogPlayerLevel> test = logPlayerLevelService.queryList(page, null, null);
//        IPage<LogPlayerLevel> pageList = logPlayerLevelService.page(page, queryWrapper);
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @Override
    protected IPage<LogPlayerLevel> pageList(Page<LogPlayerLevel> page, LogPlayerLevel entity, HttpServletRequest req) {
        DateRange createDateRange = PageQueryUtils.parseRange(req.getParameterMap(), "createDate");
        RangeValue<BigDecimal> levelRange = PageQueryUtils.parseNumberRange(req.getParameterMap(), "level");
        RangeValue<BigDecimal> combatPowerRange = PageQueryUtils.parseNumberRange(req.getParameterMap(), "combatPower");
        return logPlayerLevelService.queryList(page, entity, levelRange, combatPowerRange, createDateRange);
    }

    @Override
    protected void onload(List<LogPlayerLevel> pageList) {
        Date current = DateUtils.now();
        pageList.forEach(e -> e.setPlayDays(DateUtils.daysBetween(e.getRegisterTime(), current)));
    }

    @AutoLog(value = "境界日志-添加")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody LogPlayerLevel logPlayerLevel) {
        logPlayerLevelService.save(logPlayerLevel);
        return Result.OK("添加成功！");
    }

    @AutoLog(value = "境界日志-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody LogPlayerLevel logPlayerLevel) {
        logPlayerLevelService.updateById(logPlayerLevel);
        return Result.OK("编辑成功!");
    }

    @AutoLog(value = "境界日志-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        logPlayerLevelService.removeById(id);
        return Result.OK("删除成功!");
    }

    @AutoLog(value = "境界日志-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.logPlayerLevelService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    @GetMapping(value = "/queryById")
    public Result<LogPlayerLevel> queryById(@RequestParam(name = "id", required = true) String id) {
        LogPlayerLevel logPlayerLevel = logPlayerLevelService.getById(id);
        if (logPlayerLevel == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(logPlayerLevel);
    }

    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LogPlayerLevel logPlayerLevel) {
        return super.exportXls(request, logPlayerLevel, LogPlayerLevel.class, "境界日志");
    }

//    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
//    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
//        return super.importExcel(request, response, LogPlayerLevel.class);
//    }

}
