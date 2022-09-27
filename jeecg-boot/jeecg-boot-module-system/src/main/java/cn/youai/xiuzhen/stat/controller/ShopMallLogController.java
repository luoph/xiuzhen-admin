package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.server.conf.ConfItem;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.core.controller.SimplePageController;
import cn.youai.xiuzhen.stat.entity.ShopMallProduct;
import cn.youai.xiuzhen.stat.service.IShopMallRecordService;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.TimeConstant;
import org.jeecg.modules.utils.GameConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/17 18:34
 */
@Slf4j
@RestController
@RequestMapping("game/stat/shopMallLog")
public class ShopMallLogController extends SimplePageController<ShopMallProduct> {

    @Autowired
    private IShopMallRecordService shopMallLogService;

    @AutoLog(value = "商店销售-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ShopMallProduct entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @Override
    protected IPage<ShopMallProduct> pageList(Page<ShopMallProduct> page, ShopMallProduct entity, HttpServletRequest req) {
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "countDate");
        int serverId = entity.getServerId() != null ? entity.getServerId() : 0;
        if (serverId > 0) {
            entity.setChannel(null);
        }
        int tabId = entity.getTabId() != null ? entity.getTabId() : 0;

        List<ShopMallProduct> dateList = shopMallLogService.queryShopMallProductGroupByDate(entity.getChannel(),
                serverId, tabId, dateRange.getStart(), dateRange.getEnd());
        dateList = dateList.stream().filter(t -> t.getCountDate() != null).collect(Collectors.toList());

        // 按商店类型+日期汇总
        Map<String, List<ShopMallProduct>> dateMap = dateList.stream().collect(Collectors.groupingBy(
                ShopMallProduct::groupByTypeAndDate, HashMap::new, Collectors.toCollection(ArrayList::new)));
        dateMap.forEach((k, v) -> {
            long totalNum = v.size() > 0 ? v.stream().mapToLong(ShopMallProduct::getCostNum).sum() : 0;
            long totalBuyNum = v.size() > 0 ? v.stream().mapToLong(ShopMallProduct::getBuyNum).sum() : 0;
            v.forEach(t -> t.setTotalNum(totalNum).setTotalBuyNum(totalBuyNum)
                    .setStatTime(DateUtils.formatDate(t.getCountDate(), TimeConstant.DEFAULT_DATE_FORMAT)));
        });

        List<ShopMallProduct> itemIdList = shopMallLogService.queryShopMallProductGroupByItemId(entity.getChannel(),
                serverId, tabId, dateRange.getStart(), dateRange.getEnd());
        itemIdList = itemIdList.stream().filter(t -> t.getCountDate() != null).collect(Collectors.toList());
        long totalNum = itemIdList.stream().mapToLong(ShopMallProduct::getCostNum).sum();
        long totalBuyNum = itemIdList.stream().mapToLong(ShopMallProduct::getBuyNum).sum();
        itemIdList.forEach(t -> t.setTotalNum(totalNum).setTotalBuyNum(totalBuyNum).setStatTime("汇总"));
        // 按照消耗货币数量排序
        itemIdList.sort(Comparator.comparing(ShopMallProduct::getTabId)
                .thenComparing(ShopMallProduct::getCostNum, Comparator.reverseOrder()));

        List<ShopMallProduct> records = new ArrayList<>();
        dateMap.values().forEach(records::addAll);

        // 按照消耗货币数量排序
        records.sort(Comparator.comparing(ShopMallProduct::getCountDate, Comparator.reverseOrder())
                .thenComparing(ShopMallProduct::getTabId)
                .thenComparing(ShopMallProduct::getCostNum, Comparator.reverseOrder()));

        records.addAll(itemIdList);

        return PageQueryUtils.makePage(records);
    }

    @AutoLog(value = "商店销售-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req, ShopMallProduct entity) {
        return super.exportXls(req, entity, ShopMallProduct.class, "商店销售");
    }

    @Override
    protected void onload(ShopMallProduct entity) {
        ConfItem product = GameConfigUtils.getItemById(entity.getItemId());
        if (product != null) {
            entity.setItemName(product.getName());
        }

        ConfItem currency = GameConfigUtils.getItemById(entity.getCostItem());
        if (currency != null) {
            entity.setCostName(currency.getName());
        }
    }
}
