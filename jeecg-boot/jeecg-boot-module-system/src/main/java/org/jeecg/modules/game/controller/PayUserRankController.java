package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.PayUserRank;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IPayUserRankService;
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
 * @description 充值用户排行数据统计
 * @date 2020-10-09
 */
@Slf4j
@RestController
@RequestMapping("game/payUserRank")
public class PayUserRankController extends JeecgController<PayUserRank, IPayUserRankService> {

	@Autowired
	private IPayUserRankService payUserRankService;

	@Autowired
	private IGameChannelService gameChannelService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pageNo 页码
	 * @param pageSize 分页大小
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "充值用户排行数据统计-列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@RequestParam(name = "payTimeBegin", defaultValue = "") String payTimeBegin,
								    @RequestParam(name = "payTimeEnd", defaultValue = "") String payTimeEnd,
								    @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
								    @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
								    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
								    ) {
		Page<PayUserRank> page = new Page<>(pageNo, pageSize);
		if (StringUtils.isEmpty(payTimeBegin) && StringUtils.isEmpty(payTimeEnd) && serverId == 0 && channelId == 0) {
			return Result.ok(page);
		}
		String channel = gameChannelService.queryChannelNameById(channelId);
		List<PayUserRank> payUserRankList = payUserRankService.queryUserRankByDateRange(payTimeBegin, payTimeEnd, serverId, channel);
		page.setRecords(payUserRankList).setTotal(payUserRankList.size());
		return Result.ok(page);
	}


	/**
	 * 分页列表查询
	 *
	 * @param pageNo 页码
	 * @param pageSize 分页大小
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "付费排行-列表查询")
	@GetMapping(value = "/payRank")
	public Result<?> payRank(PayUserRank payUserRank,
								   @RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
								   @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
								   @RequestParam(name = "days", defaultValue = "0") int days,
								   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
								   @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
								   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
									) {
		Page<PayUserRank> page = new Page<>(pageNo, pageSize);
		if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
			return Result.ok(page);
		}
		String channel = gameChannelService.queryChannelNameById(channelId);
		List<PayUserRank> payUserRankList = payUserRankService.queryPayRankByDateRange(rangeDateBegin, rangeDateEnd, days, serverId, channel);
		page.setRecords(payUserRankList).setTotal(payUserRankList.size());
		return Result.ok(page);
	}
	
	/**
	 * 添加
	 *
	 * @param payUserRank 数据实体
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "充值用户排行数据统计-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PayUserRank payUserRank) {
		payUserRankService.save(payUserRank);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param payUserRank 数据实体
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "充值用户排行数据统计-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PayUserRank payUserRank) {
		payUserRankService.updateById(payUserRank);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id 实体id
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "充值用户排行数据统计-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id") String id) {
		payUserRankService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids id列表，使用','分割的字符串
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "充值用户排行数据统计-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
		this.payUserRankService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id 实体id
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "充值用户排行数据统计-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id") String id) {
		PayUserRank payUserRank = payUserRankService.getById(id);
		if(payUserRank == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(payUserRank);
	}

    /**
     * 导出excel
     *
   * @param request 请求
   * @param payUserRank 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PayUserRank payUserRank) {
        return super.exportXls(request, payUserRank, PayUserRank.class, "充值用户排行数据统计");
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
        return super.importExcel(request, response, PayUserRank.class);
    }

}
