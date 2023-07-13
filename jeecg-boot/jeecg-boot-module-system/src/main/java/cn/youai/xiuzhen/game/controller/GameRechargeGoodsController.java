package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.Response;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameRechargeGoods;
import cn.youai.xiuzhen.game.entity.ImportTextVO;
import cn.youai.xiuzhen.game.service.IGameRechargeGoodsService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_recharge_goods
 * @date 2021-04-16
 */
@Slf4j
@RestController
@RequestMapping("game/gameRechargeGoods")
public class GameRechargeGoodsController extends JeecgController<GameRechargeGoods, IGameRechargeGoodsService> {

    @Autowired
    private IGameServerService gameServerService;

    @Value("${app.goods.refresh:/rechargeGoods/update}")
    private String goodsRefresh;

    @Value("${app.folder.temp}")
    private String tempFolder;

    /**
     * 分页列表查询
     */
    @AutoLog(value = "充值商品-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameRechargeGoods entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "充值商品-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameRechargeGoods entity) {
        return super.add(entity);
    }

    @AutoLog(value = "充值商品-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameRechargeGoods entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "充值商品-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "充值商品-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "充值商品-删除全部")
    @DeleteMapping(value = "/deleteAll")
    public Result<?> deleteAll() {
        service.remove(null);
        return Result.ok("删除全部成功！");
    }

    @AutoLog(value = "充值商品-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "充值商品-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameRechargeGoods entity) {
        return super.exportXls(request, entity, GameRechargeGoods.class, "充值商品");
    }

    @AutoLog(value = "充值商品-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameRechargeGoods.class, "充值商品");
    }

    /**
     * 商品下拉选项列表
     */
    @GetMapping(value = "/loadGoodsOptions")
    public Result<List<GameRechargeGoods>> loadGoodsOptions() {
        Result<List<GameRechargeGoods>> result = new Result<>();
        List<GameRechargeGoods> goodsList;
        LambdaQueryWrapper<GameRechargeGoods> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(GameRechargeGoods::getGoodsId, GameRechargeGoods::getName);
        try {
            goodsList = service.list(queryWrapper);
            result.setSuccess(true);
            result.setResult(goodsList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "充值商品-刷新商品配置")
    @GetMapping(value = "/updateGoods")
    public Result<?> updateGoods() {
        service.refreshConfig();
        Set<Integer> onlineServerIds = gameServerService.getOnlineServerIds();
        Map<Integer, Response> responseMap = gameServerService.getUrl(onlineServerIds, goodsRefresh);
        log.info("updateGoods onlineServerIds:{}, responseMap:{}", onlineServerIds, responseMap);
        return Result.ok("刷新成功!");
    }

    @AutoLog(value = "充值商品-导入文本")
    @RequestMapping(value = "/importText", method = RequestMethod.POST)
    public Result<?> importText(@RequestBody ImportTextVO vo, HttpServletRequest request, HttpServletResponse response) {
        String fileName = tempFolder + File.separator + GameRechargeGoods.class.getSimpleName() + ".xls";
        List<GameRechargeGoods> goodsList = ExcelUtils.importFromExcelText(vo.getText(), fileName, GameRechargeGoods.class);
        if (CollUtil.isNotEmpty(goodsList)) {
            for (GameRechargeGoods goods : goodsList) {
                goods.setCreateTime(DateUtils.now());
            }
            service.saveBatch(goodsList);
        }
        return Result.ok(vo);
    }
}
