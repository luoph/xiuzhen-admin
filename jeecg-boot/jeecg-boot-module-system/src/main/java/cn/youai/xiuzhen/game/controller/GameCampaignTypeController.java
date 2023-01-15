package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaign;
import cn.youai.xiuzhen.game.entity.GameCampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeBase;
import cn.youai.xiuzhen.game.service.IGameCampaignService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.ExcelUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动类型配置
 * @date 2020-10-15
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignType")
public class GameCampaignTypeController extends JeecgController<GameCampaignType, IGameCampaignTypeService> {

    @Autowired
    private IGameCampaignService gameCampaignService;

    @AutoLog(value = "活动类型配置-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignType entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "活动类型配置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignType entity) {
        String eggsIntegralGoods = entity.getEggsIntegralGoods();
        if (entity.getType() == CampaignType.THROWING_EGGS.getType() && StringUtils.isBlank(eggsIntegralGoods)) {
            return Result.error("积分商品丢失!");
        }
        return super.add(entity);
    }

    @AutoLog(value = "活动类型配置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignType entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "活动类型配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "活动类型配置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "活动类型配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameCampaignType record = service.getById(id);
        if (record == null) {
            return Result.error("未找到对应数据");
        }
        service.fillTabDetail(record, true);
        return Result.ok(record);
    }

    /**
     * 导出excel
     *
     * @param request          请求
     * @param gameCampaignType 实体
     */
    @AutoLog(value = "活动类型配置-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignType gameCampaignType) {
        return super.exportXls(request, gameCampaignType, GameCampaignType.class, "活动类型配置");
    }

    /**
     * 活动明细-一键导入(导入多种类型的子活动明细)
     * 注: 该接口只能导入GameCampaignTypeBaseDetail的子类
     */
    @AutoLog(value = "活动类型配置-一键导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(@RequestParam(name = "campaignId") Long campaignId,
                                 HttpServletRequest request, HttpServletResponse response) {

        GameCampaign gameCampaign = gameCampaignService.getById(campaignId);
        if (gameCampaign == null) {
            return Result.error("找不到主活动配置");
        }

        List<GameCampaignType> typeList = service.list(Wrappers.<GameCampaignType>lambdaQuery().eq(GameCampaignType::getCampaignId, campaignId).orderByAsc(GameCampaignType::getSort));
        if (typeList.isEmpty()) {
            return Result.error("找不到子活动配置");
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile mf = multipartRequest.getFile("file");
        if (mf == null) {
            return Result.error("上传错误");
        }

        for (GameCampaignType gameCampaignType : typeList) {
            CampaignType campaignType = CampaignType.valueOf(gameCampaignType.getType());
            if (campaignType == null) {
                continue;
            }
            Class<? extends GameCampaignTypeBase> entityClass = campaignType.getEntityClass();
            if (null == entityClass) {
                continue;
            }

            try {
                List<?> dataList = ExcelUtils.readExcel(mf.getInputStream(), campaignType.getName(), entityClass);
                log.info("name:{}, dataList:{}", campaignType.getName(), dataList);

                List<GameCampaignTypeBase> saveOrUpdateEntities = new ArrayList<>(dataList.size());
                for (Object obj : dataList) {
                    GameCampaignTypeBase entity = entityClass.newInstance();
                    BeanUtils.copyProperties(obj, entity);
                    if (Objects.equals(entity.getCampaignId(), campaignId) && Objects.equals(entity.getTypeId(), gameCampaignType.getId())) {
                        entity.setCreateBy(null);
                        entity.setCreateTime(null);
                        entity.setUpdateBy(null);
                        entity.setUpdateTime(null);
                        saveOrUpdateEntities.add(entity);
                    }
                }

                if (!saveOrUpdateEntities.isEmpty()) {
                    campaignType.getBean().saveOrUpdateBatch(saveOrUpdateEntities);
                }
            } catch (IOException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return Result.error("文件导入失败！");
            }
        }
        return Result.ok("导入成功");
    }

    /**
     * 活动明细-导入
     */
    @RequestMapping(value = "/importExcel/details", method = RequestMethod.POST)
    public Result<?> importExcelDetails(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam(name = "campaignId") Long campaignId, @RequestParam(name = "typeId") Long typeId) {
        GameCampaign gameCampaign = gameCampaignService.getById(campaignId);
        if (null == gameCampaign) {
            return Result.error("找不到主活动配置");
        }
        return service.importExcel(gameCampaign, typeId, request);
    }

}
