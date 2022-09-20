package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.Response;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameRechargeGoods;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.ImportTextVO;
import cn.youai.xiuzhen.game.service.IGameRechargeGoodsService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import com.alibaba.fastjson2.JSON;
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
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @param req      请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_recharge_goods-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameRechargeGoods entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    /**
     * 添加
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_recharge_goods-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameRechargeGoods entity) {
        return super.add(entity);
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_recharge_goods-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameRechargeGoods entity) {
        return super.edit(entity);
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_recharge_goods-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_recharge_goods-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_recharge_goods-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameRechargeGoods entity) {
        return super.exportXls(request, entity, GameRechargeGoods.class, "game_recharge_goods");
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
        return super.importExcel(request, response, GameRechargeGoods.class);
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


    @GetMapping(value = "/updateGoods")
    public Result<?> updateGoodsOptions() {
        List<GameServer> gameServers = gameServerService.list();
        for (GameServer gameServer : gameServers) {
            JSON.parseObject(OkHttpHelper.get(gameServer.getGmUrl() + goodsRefresh), Response.class);
        }
        return Result.ok("刷新成功!");
    }

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
