package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.GameCampaign;
import org.jeecg.modules.game.entity.GameCampaignServer;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动配置
 * @date 2020-10-15
 */
public interface GameCampaignMapper extends BaseMapper<GameCampaign> {

    /**
     * 活动子页签对应的服务器列表
     *
     * @param page       分页
     * @param campaignId 活动 id
     * @param typeId     类型
     * @param server     服务器 Id 或者服务器名称
     * @return List of {@linkplain GameCampaignServer}
     */
    IPage<GameCampaignServer> serverList(Page<?> page, @Param("campaignId") long campaignId, @Param("typeId") long typeId, @Param("server") String server);

}
