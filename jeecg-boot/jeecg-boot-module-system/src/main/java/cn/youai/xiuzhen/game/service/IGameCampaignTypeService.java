package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameCampaign;
import cn.youai.xiuzhen.game.entity.GameCampaignType;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动类型配置
 * @date 2020-10-15
 */
public interface IGameCampaignTypeService extends IService<GameCampaignType> {

    /**
     * 填充具体的活动配置信息
     *
     * @param model {@linkplain GameCampaignType}
     * @param merge 是否合并子活动信息到父容器（例如 buff 类型）
     */
    void fillTabDetail(GameCampaignType model, boolean merge);

    /**
     * 更新活动具体配置信息
     *
     * @param model {@linkplain GameCampaignType}
     */
    void updateTabDetail(GameCampaignType model);

    /**
     * 复制
     *
     * @param model
     * @param copyCampaignId
     */
    void duplicate(GameCampaignType model, long copyCampaignId);

    Result<?> importExcel(GameCampaign gameCampaign, Long typeId, HttpServletRequest request);
    Result<?> importExcel(GameCampaign gameCampaign, Long typeId, HttpServletRequest request, String name, Class<? extends IService> serviceClass);
}
