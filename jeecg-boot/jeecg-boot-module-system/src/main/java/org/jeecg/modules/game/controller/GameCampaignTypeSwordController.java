package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import freemarker.template.utility.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.modules.game.entity.GameCampaignType;
import org.jeecg.modules.game.entity.GameCampaignTypeSword;
import org.jeecg.modules.game.entity.ImportTextVO;
import org.jeecg.modules.game.entity.OpenServiceCampaignRankDetailRanking;
import org.jeecg.modules.game.service.IGameCampaignTypeService;
import org.jeecg.modules.game.service.IGameCampaignTypeSwordService;
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
 * @description game_campaign_type_sword
 * @date 2021-02-23
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignTypeSword")
public class GameCampaignTypeSwordController extends JeecgController<GameCampaignTypeSword, IGameCampaignTypeSwordService> {

    @Autowired
    private IGameCampaignTypeSwordService gameCampaignTypeSwordService;

    @Autowired
    private IGameCampaignTypeService gameCampaignTypeService;

    @Value("${app.folder.temp}")
    private String tempFolder;

    /**
     * 分页列表查询
     *
     * @param gameCampaignTypeSword 数据实体
     * @param pageNo                页码
     * @param pageSize              分页大小
     * @param req                   请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_campaign_type_sword-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignTypeSword gameCampaignTypeSword,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameCampaignTypeSword> queryWrapper = QueryGenerator.initQueryWrapper(gameCampaignTypeSword, req.getParameterMap());
        Page<GameCampaignTypeSword> page = new Page<>(pageNo, pageSize);
        IPage<GameCampaignTypeSword> pageList = gameCampaignTypeSwordService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameCampaignTypeSword 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_campaign_type_sword-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignTypeSword gameCampaignTypeSword) {
        gameCampaignTypeSwordService.save(gameCampaignTypeSword);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameCampaignTypeSword 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_campaign_type_sword-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignTypeSword gameCampaignTypeSword) {
        gameCampaignTypeSwordService.updateById(gameCampaignTypeSword);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_campaign_type_sword-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameCampaignTypeSwordService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_campaign_type_sword-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameCampaignTypeSwordService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_campaign_type_sword-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameCampaignTypeSword gameCampaignTypeSword = gameCampaignTypeSwordService.getById(id);
        if (gameCampaignTypeSword == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameCampaignTypeSword);
    }

    /**
     * 导出excel
     *
     * @param request               请求
     * @param gameCampaignTypeSword 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeSword gameCampaignTypeSword) {
        return super.exportXls(request, gameCampaignTypeSword, GameCampaignTypeSword.class, "game_campaign_type_sword");
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
        return super.importExcel(request, response, GameCampaignTypeSword.class);
    }

    @RequestMapping(value = "/importText", method = RequestMethod.POST)
    public Result<?> importText(@RequestBody ImportTextVO vo, HttpServletRequest request, HttpServletResponse response) {
        GameCampaignType campaignType = gameCampaignTypeService.getById(vo.getId());
        if (campaignType == null) {
            return Result.error("未找得到对应的子活动配置!");
        }

        String fileName = tempFolder + File.separator + GameCampaignTypeSword.class.getSimpleName() + ".xls";

        List<GameCampaignTypeSword> swordList = ExcelUtils.importFromExcelText(vo.getText(), fileName, GameCampaignTypeSword.class);
        if (CollUtil.isNotEmpty(swordList)) {
            for (GameCampaignTypeSword typeSword : swordList) {
                typeSword.setId(null);
                typeSword.setCampaignId(campaignType.getCampaignId());
                typeSword.setTypeId(campaignType.getId());
                typeSword.setCreateTime(DateUtils.now());
            }
            gameCampaignTypeSwordService.saveBatch(swordList);
        }
        return Result.ok(vo);
    }
}
