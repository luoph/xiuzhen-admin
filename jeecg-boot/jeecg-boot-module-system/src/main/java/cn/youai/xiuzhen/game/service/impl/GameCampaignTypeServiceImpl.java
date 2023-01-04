package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaign;
import cn.youai.xiuzhen.game.entity.GameCampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeBase;
import cn.youai.xiuzhen.game.mapper.GameCampaignTypeMapper;
import cn.youai.xiuzhen.game.service.IGameCampaignService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动类型配置
 * @date 2020-10-15
 */
@Slf4j
@Service
@DS("master")
public class GameCampaignTypeServiceImpl extends ServiceImpl<GameCampaignTypeMapper, GameCampaignType> implements IGameCampaignTypeService {

    @Autowired
    private IGameCampaignService gameCampaignService;

    @Override
    public void fillTabDetail(GameCampaignType model, boolean merge) {
        CampaignType campaignType = CampaignType.valueOf(model.getType());
        if (null == campaignType) {
            return;
        }

        Wrapper detailQuery = Wrappers.query().eq("campaign_id", model.getCampaignId()).eq("type_id", model.getId());
        model.setDetails(campaignType.getBean().list(detailQuery));
        IService subService = campaignType.getSubBean();
        if (null != subService) {
            model.setRewardList(subService.list(detailQuery));
        }
    }

    @Deprecated
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void updateTabDetail(GameCampaignType model) {
        CampaignType campaignType = CampaignType.valueOf(model.getType());
        if (null == campaignType) {
            return;
        }

        fillTabDetail(model, false);
        List detailList = JSON.parseArray(model.getDetailsData(), campaignType.getEntityClass());
        log.info("detailList:{}", detailList);
        handleTab(model, detailList);
    }

    private void handleTab(GameCampaignType model, List<GameCampaignTypeBase> list) {
        List<GameCampaignTypeBase> addList = new ArrayList<>();
        List<Long> removeList = new ArrayList<>();

        for (GameCampaignTypeBase item : list) {
            item.setCampaignId(model.getCampaignId());
            item.setTypeId(model.getId());
            if (item.getId() == null) {
                addList.add(item);
            }
        }
        list.removeAll(addList);

        // 更新子页签
        List<GameCampaignTypeBase> updateList = new ArrayList<>(list);

        // 查找已经删除的数据
        if (!list.isEmpty()) {
            Map<Long, GameCampaignTypeBase> typeMap = list.stream().collect(Collectors.toMap(GameCampaignTypeBase::getId, Function.identity()));
            List<? extends GameCampaignTypeBase> dbList = model.getDetails();
            for (GameCampaignTypeBase item : dbList) {
                if (typeMap.containsKey(item.getId())) {
                    updateList.remove(item);
                } else {
                    removeList.add(item.getId());
                }
            }
        }

        log.debug("handleLoginTab addList:{}, updateList:{}, removeList:{}", addList, updateList, removeList);
        CampaignType campaignType = CampaignType.valueOf(model.getType());
        IService service = campaignType.getBean();
        if (CollUtil.isNotEmpty(addList)) {
            service.saveBatch(addList);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            service.updateBatchById(updateList);
        }
        if (CollUtil.isNotEmpty(removeList)) {
            service.removeByIds(removeList);
        }
    }

    @Override
    public void duplicate(GameCampaignType model, long copyCampaignId) {
        GameCampaignType copyCampaignType = new GameCampaignType(model);
        copyCampaignType.setCampaignId(copyCampaignId);
        CampaignType campaignType = CampaignType.valueOf(copyCampaignType.getType());
        if (null == campaignType || !save(copyCampaignType)) {
            return;
        }

        fillTabDetail(model, false);
        List<? extends GameCampaignTypeBase> details = model.getDetails();
        List<? extends GameCampaignTypeBase> rewards = model.getRewardList();
        if (CollUtil.isEmpty(details) && CollUtil.isEmpty(rewards)) {
            return;
        }


        if (CollUtil.isNotEmpty(details)) {
            List<GameCampaignTypeBase> copyDetails = new ArrayList<>(details.size());
            Class<? extends GameCampaignTypeBase> entityClass = campaignType.getEntityClass();
            try {
                for (GameCampaignTypeBase detail : details) {
                    GameCampaignTypeBase copyDetail = entityClass.newInstance();
                    BeanUtils.copyProperties(detail, copyDetail);
                    copyDetail.setCampaignId(copyCampaignId);
                    copyDetail.setTypeId(copyCampaignType.getId());
                    copyDetail.setId(null);
                    copyDetail.setCreateBy(null);
                    copyDetail.setCreateTime(null);
                    copyDetail.setUpdateBy(null);
                    copyDetail.setUpdateTime(null);
                    copyDetails.add(copyDetail);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            campaignType.getBean().saveBatch(copyDetails);
        }


        IService subService = campaignType.getSubBean();
        if (CollUtil.isNotEmpty(rewards) && null != subService) {
            List<GameCampaignTypeBase> copyRewards = new ArrayList<>(rewards.size());
            Class<? extends GameCampaignTypeBase> subEntityClass = campaignType.getSubEntityClass();
            try {
                for (GameCampaignTypeBase reward : rewards) {
                    GameCampaignTypeBase copyReward = subEntityClass.newInstance();
                    BeanUtils.copyProperties(reward, copyReward);
                    copyReward.setCampaignId(copyCampaignId);
                    copyReward.setTypeId(copyCampaignType.getId());
                    copyReward.setId(null);
                    copyReward.setCreateBy(null);
                    copyReward.setCreateTime(null);
                    copyReward.setUpdateBy(null);
                    copyReward.setUpdateTime(null);
                    copyRewards.add(copyReward);
                }
                subService.saveBatch(copyRewards);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result<?> importExcel(Long campaignId, Long typeId, HttpServletRequest request) {
        return importExcel(campaignId, typeId, request, null, null);
    }

    @Override
    public Result<?> importExcel(Long campaignId, Long typeId, HttpServletRequest request, String name, Class<? extends IService> serviceClass) {
        GameCampaign gameCampaign = gameCampaignService.getById(campaignId);
        if (null == gameCampaign) {
            return Result.error("找不到主活动配置");
        }

        GameCampaignType gameCampaignType = getById(typeId);
        if (null == gameCampaignType) {
            return Result.error("找不到子活动配置");
        }

        CampaignType campaignType = CampaignType.valueOf(gameCampaignType.getType());
        if (campaignType == null) {
            return Result.error("子活动类型未定义");
        }

        String sheetName = StringUtils.isNotBlank(name) ? name : campaignType.getName();

        IService service = null != serviceClass ? SpringContextUtils.getBean(serviceClass) : campaignType.getBean();
        if (null == service) {
            return Result.error("serviceClass错误, service空");
        }

        Class<? extends GameCampaignTypeBase> entityClass = service.getEntityClass();
        if (null == entityClass) {
            return Result.error("serviceClass错误, entityClass空");
        }

//        Wrapper<?> detailQuery = Wrappers.query().eq("campaign_id", campaignId).eq("type_id", typeId);
//        List<? extends GameCampaignTypeBase> details = SpringContextUtils.getBean(festivalType.getServiceClass()).list(detailQuery);

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile mf = multipartRequest.getFile("file");
        if (mf == null) {
            return Result.error("上传错误");
        }

        try {
            List<?> dataList = ExcelUtils.readExcel(mf.getInputStream(), sheetName, entityClass);
            if (dataList.isEmpty()) {
                return Result.error("导入数据为空");
            }

            List<GameCampaignTypeBase> saveOrUpdateEntities = new ArrayList<>(dataList.size());
            for (Object obj : dataList) {
                GameCampaignTypeBase entity = entityClass.newInstance();
                BeanUtils.copyProperties(obj, entity);
                if (Objects.equals(entity.getCampaignId(), campaignId) && Objects.equals(entity.getTypeId(), typeId)) {
                    entity.setCreateBy(null);
                    entity.setCreateTime(null);
                    entity.setUpdateBy(null);
                    entity.setUpdateTime(null);
                    saveOrUpdateEntities.add(entity);
                }
            }

            if (saveOrUpdateEntities.isEmpty()) {
                return Result.error("导入数据为空，主活动id/子活动id错误！");
            }
            service.saveOrUpdateBatch(saveOrUpdateEntities);
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return Result.error("文件导入失败！");
        }

        return Result.ok("导入成功");
    }
}
