package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.game.entity.PayOrderBill;
import org.jeecg.modules.game.entity.PayOrderBillVo;
import org.jeecg.modules.game.service.IPayOrderBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description pay_order
 * @date 2020-09-29
 */
@Slf4j
@RestController
@RequestMapping("game/payOrderBill")
public class PayOrderBillController extends JeecgController<PayOrderBill, IPayOrderBillService> {

	@Autowired
	private IPayOrderBillService payOrderBillService;
	
	/**
	 * 分页列表查询
	 *
	 * @param payOrderBill 数据实体
	 * @param pageNo 页码
	 * @param pageSize 分页大小
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "pay_order-列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PayOrderBill payOrderBill,
								   @RequestParam(name = "payTime_begin", defaultValue = "") String payTimeBegin,
								   @RequestParam(name = "payTime_end", defaultValue = "") String payTimeEnd,
								   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
								   @RequestParam(name = "channel", defaultValue = "0") Integer channel,
								   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
								  ) {
		BigDecimal billSum = null;
		Page<PayOrderBillVo> pageVo = new Page<>(pageNo, pageSize);
		if (StringUtils.isEmpty(payTimeBegin) && StringUtils.isEmpty(payTimeEnd) && serverId == 0 && channel == 0){
			List listVo = new ArrayList<PayOrderBillVo>();
			PayOrderBillVo payOrderBillVo = new PayOrderBillVo();
			billSum = new BigDecimal(0);
			payOrderBillVo.setBillSum(billSum).setBeginTime(payTimeBegin).setEndTime(payTimeEnd);
			listVo.add(payOrderBillVo);
			pageVo.setRecords(listVo).setTotal(listVo.size());
			return Result.ok(pageVo);
		}

		billSum = payOrderBillService.queryBillSumByDateRange(payTimeBegin, payTimeEnd, serverId, channel);
		List listVo = new ArrayList<PayOrderBillVo>();
		PayOrderBillVo payOrderBillVo = new PayOrderBillVo();
		payOrderBillVo.setBillSum(billSum).setBeginTime(payTimeBegin).setEndTime(payTimeEnd);
		listVo.add(payOrderBillVo);
		pageVo.setRecords(listVo).setTotal(listVo.size());
		return Result.ok(pageVo);
	}
	
	/**
	 * 添加
	 *
	 * @param payOrderBill 数据实体
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "pay_order-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PayOrderBill payOrderBill) {
		payOrderBillService.save(payOrderBill);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param payOrderBill 数据实体
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "pay_order-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PayOrderBill payOrderBill) {
		payOrderBillService.updateById(payOrderBill);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id 实体id
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "pay_order-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id") String id) {
		payOrderBillService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids id列表，使用','分割的字符串
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "pay_order-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
		this.payOrderBillService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id 实体id
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "pay_order-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id") String id) {
		PayOrderBill payOrderBill = payOrderBillService.getById(id);
		if(payOrderBill == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(payOrderBill);
	}

    /**
     * 导出excel
     *
   * @param request 请求
   * @param payOrderBill 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PayOrderBill payOrderBill) {
        return super.exportXls(request, payOrderBill, PayOrderBill.class, "pay_order");
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
        return super.importExcel(request, response, PayOrderBill.class);
    }

}
