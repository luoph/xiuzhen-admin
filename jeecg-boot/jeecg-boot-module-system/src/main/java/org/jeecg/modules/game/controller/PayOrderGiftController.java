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

}
