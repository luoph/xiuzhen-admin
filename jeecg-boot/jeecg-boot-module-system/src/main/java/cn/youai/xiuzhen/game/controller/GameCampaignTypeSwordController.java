package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.server.utils.SqlHelper;
import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeSword;
import cn.youai.xiuzhen.game.entity.ImportTextVO;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeSwordService;
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
 * @description 节日活动-限时剑仙
 * @date 2021-02-23
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignTypeSword")
public class GameCampaignTypeSwordController extends JeecgController<GameCampaignTypeSword, IGameCampaignTypeSwordService> {

    @Autowired
    private IGameCampaignTypeService gameCampaignTypeService;

    @Value("${app.folder.temp}")
    private String tempFolder;

    @AutoLog(value = "节日活动-限时剑仙-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignTypeSword entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "节日活动-限时剑仙-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignTypeSword entity) {
        if (entity.getUnlockCheckpointId() > 0 && entity.getUnlockCheckpointId() >= entity.getCheckpointId()) {
            return Result.error("解锁关卡配置错误");
        }
        if (null == getGameCampaignTypeSword(entity)) {
            return Result.error("解锁关卡配置错误");
        }
        return super.add(entity);
    }

    @AutoLog(value = "节日活动-限时剑仙-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignTypeSword entity) {
        if (entity.getUnlockCheckpointId() > 0 && entity.getUnlockCheckpointId() >= entity.getCheckpointId()) {
            return Result.error("解锁关卡配置错误");
        }
        if (null == getGameCampaignTypeSword(entity)) {
            return Result.error("解锁关卡配置错误");
        }
        return super.edit(entity);
    }

    private GameCampaignTypeSword getGameCampaignTypeSword(GameCampaignTypeSword entity) {
        return service.getOne(Wrappers.<GameCampaignTypeSword>lambdaQuery()
                .eq(GameCampaignTypeSword::getCampaignId, entity.getCampaignId())
                .eq(GameCampaignTypeSword::getTypeId, entity.getTypeId())
                .eq(GameCampaignTypeSword::getCheckpointId, entity.getUnlockCheckpointId())
                .last(SqlHelper.limit()));
    }

    @AutoLog(value = "节日活动-限时剑仙-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "节日活动-限时剑仙-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "节日活动-限时剑仙-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "节日活动-限时剑仙-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeSword entity) {
        return super.exportXls(request, entity, GameCampaignTypeSword.class, CampaignType.valueOfServiceClass(service.getClass()).getName());
    }

    @AutoLog(value = "节日活动-限时剑仙-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignTypeSword.class);
    }

    @AutoLog(value = "节日活动-限时剑仙-导入文本")
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
            service.saveBatch(swordList);
        }
        return Result.ok(vo);
    }
}
