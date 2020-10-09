package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.entity.DailyGiftPackageBuy;
import org.jeecg.modules.game.entity.DailyGiftPackageBuyVO;
import org.jeecg.modules.game.service.IDailyGiftPackageBuyService;
import org.jeecg.modules.game.service.IDailyGiftPackageBuyVOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description daily_gift_package_buy
 * @date 2020-09-30
 */
@Slf4j
@RestController
@RequestMapping("game/dailyGiftPackageBuy")
public class DailyGiftPackageBuyController extends JeecgController<DailyGiftPackageBuy, IDailyGiftPackageBuyService> {

	@Autowired
	private IDailyGiftPackageBuyService dailyGiftPackageBuyService;

	@Autowired
	private IDailyGiftPackageBuyVOService dailyGiftPackageBuyVOService;

	/**
	 * 分页列表查询
	 *
	 * @param dailyGiftPackageBuy 数据实体
	 * @param pageNo 页码
	 * @param pageSize 分页大小
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "daily_gift_package_buy-列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DailyGiftPackageBuy dailyGiftPackageBuy,
								   @RequestParam(name = "createTimeBegin", defaultValue = "") String createTimeBegin,
								   @RequestParam(name = "createTimeEnd", defaultValue = "") String createTimeEnd,
								   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
								   @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
								   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
								   ) {
		// TODO:后面可能会拓展一个channelId可能绑定多个serverId,所以保留serverId字段
		Page<DailyGiftPackageBuyVO> pageVo = new Page<>(pageNo, pageSize);
		if (StringUtils.isEmpty(createTimeBegin) && StringUtils.isEmpty(createTimeEnd) && serverId == 0 && channelId == 0) {
			return Result.ok(pageVo);
		}
		List<DailyGiftPackageBuyVO> dailyGiftPackageBuyVOList = dailyGiftPackageBuyVOService.queryGiftPackageByDateRange(serverId, createTimeBegin, createTimeEnd);
		pageVo.setRecords(dailyGiftPackageBuyVOList).setTotal(dailyGiftPackageBuyVOList.size());
		return Result.ok(pageVo);
	}
	
	/**
	 * 添加
	 *
	 * @param dailyGiftPackageBuy 数据实体
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "daily_gift_package_buy-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DailyGiftPackageBuy dailyGiftPackageBuy) {
		dailyGiftPackageBuyService.save(dailyGiftPackageBuy);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param dailyGiftPackageBuy 数据实体
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "daily_gift_package_buy-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DailyGiftPackageBuy dailyGiftPackageBuy) {
		dailyGiftPackageBuyService.updateById(dailyGiftPackageBuy);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id 实体id
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "daily_gift_package_buy-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id") String id) {
		dailyGiftPackageBuyService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids id列表，使用','分割的字符串
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "daily_gift_package_buy-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
		this.dailyGiftPackageBuyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id 实体id
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "daily_gift_package_buy-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id") String id) {
		DailyGiftPackageBuy dailyGiftPackageBuy = dailyGiftPackageBuyService.getById(id);
		if(dailyGiftPackageBuy == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(dailyGiftPackageBuy);
	}

    /**
     * 导出excel
     *
   * @param request 请求
   * @param dailyGiftPackageBuy 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DailyGiftPackageBuy dailyGiftPackageBuy) {
        return super.exportXls(request, dailyGiftPackageBuy, DailyGiftPackageBuy.class, "daily_gift_package_buy");
    }

    /**
     * 通过excel导入数据
     *
   * @param request 请求
   * @param response 响应
   * @return {@linkplain Result}
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, DailyGiftPackageBuy.class);
    }

}