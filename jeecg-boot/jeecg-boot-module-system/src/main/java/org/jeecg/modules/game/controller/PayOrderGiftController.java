package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.game.entity.PayOrderGift;
import org.jeecg.modules.game.entity.PayOrderGiftVO;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IPayOrderGiftService;
import org.jeecg.modules.game.service.IPayOrderGiftVOService;
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
 * @description 直充道具数据统计
 * @date 2020-09-29
 */
@Slf4j
@RestController
@RequestMapping("game/payOrderGift")
public class PayOrderGiftController extends JeecgController<PayOrderGift, IPayOrderGiftService> {

    @Autowired
    private IPayOrderGiftService payOrderGiftService;

    @Autowired
    private IPayOrderGiftVOService payOrderGiftVOService;

    @Autowired
    private IGameChannelService gameChannelService;

    /**
     * 分页列表查询
     *
     * @param payOrderGift 数据实体
     * @param pageNo       页码
     * @param pageSize     分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "直充道具-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PayOrderGift payOrderGift,
                                   @RequestParam(name = "payTimeBegin", defaultValue = "") String payTimeBegin,
                                   @RequestParam(name = "payTimeEnd", defaultValue = "") String payTimeEnd,
                                   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                   @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
                                    ) {
        Page<PayOrderGiftVO> pageVo = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(payTimeBegin) && StringUtils.isEmpty(payTimeEnd) && serverId == 0 && channelId == 0) {
            return Result.ok(pageVo);
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<PayOrderGiftVO> payOrderGiftVOList = payOrderGiftVOService.queryGiftByDateRange(payTimeBegin, payTimeEnd, serverId, channel);
        pageVo.setRecords(payOrderGiftVOList).setTotal(payOrderGiftVOList.size());
        return Result.ok(pageVo);
    }

    /**
     * 添加
     *
     * @param payOrderGift 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "直充道具-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody PayOrderGift payOrderGift) {
        payOrderGiftService.save(payOrderGift);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param payOrderGift 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "直充道具-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody PayOrderGift payOrderGift) {
        payOrderGiftService.updateById(payOrderGift);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "直充道具-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        payOrderGiftService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "直充道具-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.payOrderGiftService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "直充道具-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        PayOrderGift payOrderGift = payOrderGiftService.getById(id);
        if (payOrderGift == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(payOrderGift);
    }

    /**
     * 导出excel
     *
     * @param request      请求
     * @param payOrderGift 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PayOrderGift payOrderGift) {
        return super.exportXls(request, payOrderGift, PayOrderGift.class, "消费礼包数据统计");
    }

    /**
     * 通过excel导入数据
     *
     * @param request  请求
     * @param response 响应
     * @return {@linkplain Result}
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, PayOrderGift.class);
    }

}
