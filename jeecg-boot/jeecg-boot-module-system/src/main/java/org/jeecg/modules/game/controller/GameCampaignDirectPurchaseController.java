package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.modules.game.entity.GameCampaignDirectPurchase;
import org.jeecg.modules.game.entity.GameCampaignType;
import org.jeecg.modules.game.entity.ImportTextVO;
import org.jeecg.modules.game.service.IGameCampaignDirectPurchaseService;
import org.jeecg.modules.game.service.IGameCampaignTypeService;
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
 * @description 直购礼包
 * @date 2021-04-15
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignDirectPurchase")
public class GameCampaignDirectPurchaseController extends JeecgController<GameCampaignDirectPurchase, IGameCampaignDirectPurchaseService> {

    @Autowired
    private IGameCampaignDirectPurchaseService gameCampaignDirectPurchaseService;

    @Autowired
    private IGameCampaignTypeService gameCampaignTypeService;

    @Value("${app.folder.temp}")
    private String tempFolder;

    /**
     * 分页列表查询
     *
     * @param gameCampaignDirectPurchase 数据实体
     * @param pageNo                     页码
     * @param pageSize                   分页大小
     * @param req                        请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "直购礼包-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignDirectPurchase gameCampaignDirectPurchase,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameCampaignDirectPurchase> queryWrapper = QueryGenerator.initQueryWrapper(gameCampaignDirectPurchase, req.getParameterMap());
        Page<GameCampaignDirectPurchase> page = new Page<>(pageNo, pageSize);
        IPage<GameCampaignDirectPurchase> pageList = gameCampaignDirectPurchaseService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameCampaignDirectPurchase 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "直购礼包-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignDirectPurchase gameCampaignDirectPurchase) {
        gameCampaignDirectPurchaseService.save(gameCampaignDirectPurchase);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameCampaignDirectPurchase 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "直购礼包-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignDirectPurchase gameCampaignDirectPurchase) {
        gameCampaignDirectPurchaseService.updateById(gameCampaignDirectPurchase);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "直购礼包-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameCampaignDirectPurchaseService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "直购礼包-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameCampaignDirectPurchaseService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "直购礼包-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameCampaignDirectPurchase gameCampaignDirectPurchase = gameCampaignDirectPurchaseService.getById(id);
        if (gameCampaignDirectPurchase == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameCampaignDirectPurchase);
    }

    /**
     * 导出excel
     *
     * @param request                    请求
     * @param gameCampaignDirectPurchase 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignDirectPurchase gameCampaignDirectPurchase) {
        return super.exportXls(request, gameCampaignDirectPurchase, GameCampaignDirectPurchase.class, "直购礼包");
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
        return super.importExcel(request, response, GameCampaignDirectPurchase.class);
    }


    @RequestMapping(value = "/importText", method = RequestMethod.POST)
    public Result<?> importText(@RequestBody ImportTextVO vo, HttpServletRequest request, HttpServletResponse response) {
        GameCampaignType campaignType = gameCampaignTypeService.getById(vo.getId());
        if (campaignType == null) {
            return Result.error("未找得到对应的子活动配置!");
        }

        String fileName = tempFolder + File.separator + GameCampaignDirectPurchase.class.getSimpleName() + ".xls";

        List<GameCampaignDirectPurchase> directPurchaseList = ExcelUtils.importFromExcelText(vo.getText(), fileName, GameCampaignDirectPurchase.class);
        if (CollUtil.isNotEmpty(directPurchaseList)) {
            for (GameCampaignDirectPurchase directPurchase : directPurchaseList) {
                directPurchase.setId(null);
                directPurchase.setCampaignId(campaignType.getCampaignId());
                directPurchase.setTypeId(campaignType.getId());
                directPurchase.setCreateTime(DateUtils.now());
            }
            gameCampaignDirectPurchaseService.saveBatch(directPurchaseList);
        }
        return Result.ok(vo);
    }
}
