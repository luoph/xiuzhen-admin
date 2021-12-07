package org.jeecg.modules.player.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.youai.entities.GamePlayer;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameStoryAnalysis;
import org.jeecg.modules.player.entity.GameOrder;
import org.jeecg.modules.player.entity.GameRegisterInfo;
import org.jeecg.modules.player.service.IGameRegisterInfoService;
import org.jeecg.modules.player.service.IPayOrderService;
import org.jeecg.modules.player.service.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.terracotta.quartz.wrappers.WrapperFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
@Slf4j
@RestController
@RequestMapping("player/payOrder")
public class PayOrderController extends JeecgController<GameOrder, IPayOrderService> {

    @Autowired
    private IPayOrderService payOrderService;

    @Autowired
    private IGameRegisterInfoService registerInfoService;

    /**
     * 分页列表查询
     *
     * @param gameOrder 数据实体
     * @param pageNo    页码
     * @param pageSize  分页大小
     * @param req       请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "充值订单-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameOrder gameOrder,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameOrder> queryWrapper = QueryGenerator.initQueryWrapper(gameOrder, req.getParameterMap());
        Page<GameOrder> page = new Page<>(pageNo, pageSize);
        IPage<GameOrder> pageList = payOrderService.page(page, queryWrapper);
        if (pageList.getRecords() != null && pageList.getRecords().size() > 0) {
            HashSet<Long> playerIds = new HashSet<>(pageList.getRecords().size());
            pageList.getRecords().forEach(e -> playerIds.add(e.getPlayerId()));

            LambdaQueryWrapper<GameRegisterInfo> query = Wrappers.lambdaQuery();
            query.select(GameRegisterInfo::getPlayerId, GameRegisterInfo::getName);
            query.in(GameRegisterInfo::getPlayerId, playerIds);
            query.groupBy(GameRegisterInfo::getPlayerId);
            List<GameRegisterInfo> list = registerInfoService.list(query);

            Map<Long, String> nameByPlayerId = CollectionUtil.isNotEmpty(list) ?
                    list.stream().collect(Collectors.toMap(GameRegisterInfo::getPlayerId, GameRegisterInfo::getName,
                            (item1, item2) -> item2)) : new HashMap<>(list.size());

            pageList.getRecords().forEach(e -> e.setPlayerName(nameByPlayerId.get(e.getPlayerId()) != null ? nameByPlayerId.get(e.getPlayerId()) : ""));
        }
        return Result.ok(pageList);
    }

//    /**
//     * 添加
//     *
//     * @param payOrder 数据实体
//     * @return {@linkplain Result}
//     */
//    @AutoLog(value = "充值订单-添加")
//    @PostMapping(value = "/add")
//    public Result<?> add(@RequestBody PayOrder payOrder) {
//        payOrderService.save(payOrder);
//        return Result.ok("添加成功！");
//    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "充值订单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameOrder gameOrder = payOrderService.getById(id);
        if (gameOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameOrder);
    }

    /**
     * 导出excel
     *
     * @param request   请求
     * @param gameOrder 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameOrder gameOrder) {
        return super.exportXls(request, gameOrder, GameOrder.class, "充值订单");
    }

    @RequestMapping("download")
    public void download(HttpServletResponse response, @RequestBody JSONObject jsonObject) throws IOException {
        GameOrder gameOrder = JSON.parseObject(jsonObject.toJSONString(), GameOrder.class);

        Map<String, String[]> data = new HashMap<>(16);
        Iterator it = jsonObject.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            String[] a = new String[1];
            a[0] = String.valueOf(entry.getValue());
            data.put(entry.getKey(), a);
        }
        QueryWrapper<GameOrder> queryWrapper = QueryGenerator.initQueryWrapper(gameOrder, data);
        List<GameOrder> gameOrderList = payOrderService.list(queryWrapper);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("excel导出文件名", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), GameOrder.class).sheet("模板").doWrite(gameOrderList);
    }
}
