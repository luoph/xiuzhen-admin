package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.entity.GameCampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeMarryRank;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeMarryRankService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日活动-结义排行榜
 * @date 2021-06-20
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignTypeMarryRank")
public class GameCampaignTypeMarryRankController extends JeecgController<GameCampaignTypeMarryRank, IGameCampaignTypeMarryRankService> {

    @Autowired
    private IGameCampaignTypeService campaignTypeService;

    @AutoLog(value = "节日活动-结义排行榜-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignTypeMarryRank entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<GameCampaignTypeMarryRank> page = pageList(entity, pageNo, pageSize, req);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            GameCampaignType gameCampaignType = campaignTypeService.getOne(Wrappers.<GameCampaignType>lambdaQuery().eq(GameCampaignType::getId, entity.getTypeId()));
            if (null != gameCampaignType) {
                page.getRecords().forEach(e -> e.setType(gameCampaignType.getType()));
            }
        }
        return Result.ok(page);
    }

    @AutoLog(value = "节日活动-结义排行榜-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignTypeMarryRank entity) {
        return super.add(entity);
    }

    @AutoLog(value = "节日活动-结义排行榜-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignTypeMarryRank entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "节日活动-结义排行榜-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "节日活动-结义排行榜-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "节日活动-结义排行榜-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "节日活动-结义排行榜-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeMarryRank entity) {
        return super.exportXls(request, entity, GameCampaignTypeMarryRank.class, "节日活动-结义排行榜");
    }

    @AutoLog(value = "节日活动-结义排行榜-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignTypeMarryRank.class);
    }

}
