package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.modules.game.constant.CampaignFestivalType;
import org.jeecg.modules.game.entity.*;
import org.jeecg.modules.game.service.*;
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
import java.util.Date;
import java.util.List;

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

    @Autowired
    private IGameCampaignTypeLoginService campaignTypeLoginService;
    @Autowired
    private IGameCampaignTypeRechargeService campaignTypeRechargeService;
    @Autowired
    private IGameCampaignTypeExchangeService campaignTypeExchangeService;
    @Autowired
    private IGameCampaignTypeTaskService campaignTypeTaskService;
    @Autowired
    private IGameCampaignTypeBuffService campaignTypeBuffService;

    /**
     * 分页列表查询
     *
     * @param entity 数据实体
     * @param pageNo           页码
     * @param pageSize         分页大小
     * @param req              请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动类型配置-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignType entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    /**
     * 添加
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动类型配置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignType entity) {
        String eggsIntegralGoods = entity.getEggsIntegralGoods();
        if (entity.getType() == CampaignFestivalType.THROWING_EGGS.getValue() && StringUtils.isBlank(eggsIntegralGoods)) {
            return Result.error("积分商品丢失!");
        }
        return super.add(entity);
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动类型配置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignType entity) {
        return super.edit(entity);
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "活动类型配置-通过id删除")
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
    @AutoLog(value = "活动类型配置-批量删除")
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
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignType gameCampaignType) {
        return super.exportXls(request, gameCampaignType, GameCampaignType.class, "活动类型配置");
    }

    /**
     * 通过excel导入数据
     *
     * @param request  请求
     * @param response 响应
     * @return {@linkplain Result}
     */
    @SuppressWarnings({"unchecked"})
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(@RequestParam(name = "campaignId") Long campaignId,
                                 HttpServletRequest request, HttpServletResponse response) {

        GameCampaign gameCampaign = gameCampaignService.getById(campaignId);
        if (gameCampaign == null) {
            return Result.error("找不到活动配置");
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        // 获取上传文件对象∂∂
        MultipartFile mf = multipartRequest.getFile("file");
        if (mf == null) {
            return Result.error("上传错误");
        }

        Wrapper<GameCampaignType> query = Wrappers.<GameCampaignType>lambdaQuery()
                .eq(GameCampaignType::getCampaignId, campaignId)
                .orderByAsc(GameCampaignType::getSort);

        List<GameCampaignType> typeList = service.list(query);
        for (GameCampaignType campaignType : typeList) {
            CampaignFestivalType festivalType = CampaignFestivalType.valueOf(campaignType.getType());
            if (festivalType == null) {
                continue;
            }

            try {
                List<?> dataList = ExcelUtils.readExcel(mf.getInputStream(), festivalType.getName(), festivalType.getExcelClass());
                log.info("name:{}, dataList:{}", festivalType.getName(), dataList);

                if (festivalType.getTableClass() == GameCampaignTypeLogin.class) {
                    List<GameCampaignTypeLogin> entities = new ArrayList<>();
                    for (Object obj : dataList) {
                        GameCampaignTypeLogin entity = new GameCampaignTypeLogin();
                        BeanUtils.copyProperties(obj, entity);
                        entity.setCampaignId(campaignId);
                        entity.setTypeId(campaignType.getId());
                        entity.setCreateTime(new Date());
                        entities.add(entity);
                    }
                    campaignTypeLoginService.saveBatch(entities);
                } else if (festivalType.getTableClass() == GameCampaignTypeRecharge.class) {
                    List<GameCampaignTypeRecharge> entities = new ArrayList<>();
                    for (Object obj : dataList) {
                        GameCampaignTypeRecharge entity = new GameCampaignTypeRecharge();
                        BeanUtils.copyProperties(obj, entity);
                        entity.setCampaignId(campaignId);
                        entity.setTypeId(campaignType.getId());
                        entity.setCreateTime(new Date());
                        entities.add(entity);
                    }
                    campaignTypeRechargeService.saveBatch(entities);
                } else if (festivalType.getTableClass() == GameCampaignTypeExchange.class) {
                    List<GameCampaignTypeExchange> entities = new ArrayList<>();
                    for (Object obj : dataList) {
                        GameCampaignTypeExchange entity = new GameCampaignTypeExchange();
                        BeanUtils.copyProperties(obj, entity);
                        entity.setCampaignId(campaignId);
                        entity.setTypeId(campaignType.getId());
                        entity.setCreateTime(new Date());
                        entities.add(entity);
                    }
                    campaignTypeExchangeService.saveBatch(entities);
                } else if (festivalType.getTableClass() == GameCampaignTypeTask.class) {
                    List<GameCampaignTypeTask> entities = new ArrayList<>();
                    for (Object obj : dataList) {
                        GameCampaignTypeTask entity = new GameCampaignTypeTask();
                        BeanUtils.copyProperties(obj, entity);
                        entity.setCampaignId(campaignId);
                        entity.setTypeId(campaignType.getId());
                        entity.setCreateTime(new Date());
                        entities.add(entity);
                    }
                    campaignTypeTaskService.saveBatch(entities);
                } else if (festivalType.getTableClass() == GameCampaignTypeBuff.class) {
                    List<GameCampaignTypeBuff> entities = new ArrayList<>();
                    for (Object obj : dataList) {
                        GameCampaignTypeBuff entity = new GameCampaignTypeBuff();
                        BeanUtils.copyProperties(obj, entity);
                        entity.setCampaignId(campaignId);
                        entity.setTypeId(campaignType.getId());
                        entity.setCreateTime(new Date());
                        entities.add(entity);
                    }
                    campaignTypeBuffService.saveBatch(entities);
                }

            } catch (IOException e) {
                e.printStackTrace();
                return Result.error("文件导入失败！");
            }
        }
        return Result.ok("导入成功");
    }

}
