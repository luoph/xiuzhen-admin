package cn.youai.xiuzhen.stat.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.youai.entities.GamePlayer;
import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import cn.youai.xiuzhen.stat.service.IGameOrderStatService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
        if (pageList.getRecords() != null && pageList.getRecords().size() > 0) {
            HashSet<Long> playerIds = new HashSet<>(pageList.getRecords().size());
            pageList.getRecords().forEach(e -> playerIds.add(e.getPlayerId()));

            LambdaQueryWrapper<GamePlayer> query = Wrappers.lambdaQuery();
            query.select(GamePlayer::getPlayerId, GamePlayer::getNickname);
            query.in(GamePlayer::getPlayerId, playerIds);
            query.groupBy(GamePlayer::getPlayerId);
            List<GamePlayer> list = playerService.list(query);

            Map<Long, String> nameMap = CollectionUtil.isNotEmpty(list) ?
                    list.stream().collect(Collectors.toMap(GamePlayer::getPlayerId, GamePlayer::getNickname,
                            (item1, item2) -> item2)) : new HashMap<>(list.size());

            pageList.getRecords().forEach(e -> e.setNickname(nameMap.get(e.getPlayerId())));
        }
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

    /**
     * 导出excel
     *
     * @param request 请求
     * @param entity  实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameOrder entity) {
        return super.exportXls(request, entity, GameOrder.class, "充值订单");
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
        List<GameOrder> gameOrderList = service.list(queryWrapper);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("excel导出文件名", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), GameOrder.class).sheet("模板").doWrite(gameOrderList);
    }
}
