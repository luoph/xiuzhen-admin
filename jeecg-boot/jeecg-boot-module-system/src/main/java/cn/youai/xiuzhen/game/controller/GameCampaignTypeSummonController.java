package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeSummon;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeSummonService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
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
import java.util.Arrays;

/**
 * @Description: 节日活动-召唤活动
 * @Author: jeecg-boot
 * @Date: 2023-04-12
 * @Version: V1.0
 */
@Api(tags = "节日活动-召唤活动")
@RestController
@RequestMapping("/game/gameCampaignTypeSummon")
@Slf4j
public class GameCampaignTypeSummonController extends JeecgController<GameCampaignTypeSummon, IGameCampaignTypeSummonService> {
    @Autowired
    private IGameCampaignTypeSummonService gameCampaignTypeSummonService;

    /**
     * 分页列表查询
     *
     * @param gameCampaignTypeSummon
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "节日活动-召唤活动-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<GameCampaignTypeSummon>> queryPageList(GameCampaignTypeSummon gameCampaignTypeSummon,
                                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                               HttpServletRequest req) {
        QueryWrapper<GameCampaignTypeSummon> queryWrapper = QueryGenerator.initQueryWrapper(gameCampaignTypeSummon, req.getParameterMap());
        Page<GameCampaignTypeSummon> page = new Page<GameCampaignTypeSummon>(pageNo, pageSize);
        IPage<GameCampaignTypeSummon> pageList = gameCampaignTypeSummonService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param gameCampaignTypeSummon
     * @return
     */
    @AutoLog(value = "节日活动-召唤活动-添加")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_summon:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody GameCampaignTypeSummon gameCampaignTypeSummon) {
        gameCampaignTypeSummonService.save(gameCampaignTypeSummon);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameCampaignTypeSummon
     * @return
     */
    @AutoLog(value = "节日活动-召唤活动-编辑")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_summon:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody GameCampaignTypeSummon gameCampaignTypeSummon) {
        gameCampaignTypeSummonService.updateById(gameCampaignTypeSummon);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "节日活动-召唤活动-通过id删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_summon:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        gameCampaignTypeSummonService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "节日活动-召唤活动-批量删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_summon:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gameCampaignTypeSummonService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "节日活动-召唤活动-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<GameCampaignTypeSummon> queryById(@RequestParam(name = "id", required = true) String id) {
        GameCampaignTypeSummon gameCampaignTypeSummon = gameCampaignTypeSummonService.getById(id);
        if (gameCampaignTypeSummon == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gameCampaignTypeSummon);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gameCampaignTypeSummon
     */
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_summon:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeSummon gameCampaignTypeSummon) {
        return super.exportXls(request, gameCampaignTypeSummon, GameCampaignTypeSummon.class, CampaignType.valueOfServiceClass(service.getClass()).getName());
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    //@RequiresPermissions("game_campaign_type_summon:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignTypeSummon.class);
    }

}
