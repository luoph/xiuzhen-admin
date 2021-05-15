package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.commons.model.Response;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.xiuzhen.utils.DateUtils;
import com.alibaba.fastjson.JSON;
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
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.modules.game.entity.GameRechargeGoods;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.entity.ImportTextVO;
import org.jeecg.modules.game.service.IGameRechargeGoodsService;
import org.jeecg.modules.game.service.IGameServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Arrays;
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
    private IGameRechargeGoodsService gameRechargeGoodsService;

    @Autowired
    private IGameServerService gameServerService;

    @Value("${app.goods.refresh:/rechargeGoods/update}")
    private String goodsRefresh;

    @Value("${app.folder.temp}")
    private String tempFolder;

    /**
     * 分页列表查询
     *
     * @param gameRechargeGoods 数据实体
     * @param pageNo            页码
     * @param pageSize          分页大小
     * @param req               请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_recharge_goods-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameRechargeGoods gameRechargeGoods,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameRechargeGoods> queryWrapper = QueryGenerator.initQueryWrapper(gameRechargeGoods, req.getParameterMap());
        Page<GameRechargeGoods> page = new Page<>(pageNo, pageSize);
        IPage<GameRechargeGoods> pageList = gameRechargeGoodsService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameRechargeGoods 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_recharge_goods-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameRechargeGoods gameRechargeGoods) {
        gameRechargeGoodsService.save(gameRechargeGoods);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameRechargeGoods 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_recharge_goods-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameRechargeGoods gameRechargeGoods) {
        gameRechargeGoodsService.updateById(gameRechargeGoods);
        return Result.ok("编辑成功!");
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
        gameRechargeGoodsService.removeById(id);
        return Result.ok("删除成功!");
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
        this.gameRechargeGoodsService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
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
        GameRechargeGoods gameRechargeGoods = gameRechargeGoodsService.getById(id);
        if (gameRechargeGoods == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameRechargeGoods);
    }

    /**
     * 导出excel
     *
     * @param request           请求
     * @param gameRechargeGoods 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameRechargeGoods gameRechargeGoods) {
        return super.exportXls(request, gameRechargeGoods, GameRechargeGoods.class, "game_recharge_goods");
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
            goodsList = gameRechargeGoodsService.list(queryWrapper);
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
            gameRechargeGoodsService.saveBatch(goodsList);
        }
        return Result.ok(vo);
    }
}
