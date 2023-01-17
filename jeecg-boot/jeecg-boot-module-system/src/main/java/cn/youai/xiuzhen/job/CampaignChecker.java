package cn.youai.xiuzhen.job;

import cn.youai.xiuzhen.game.entity.GameCampaign;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaign;
import cn.youai.xiuzhen.game.service.IGameCampaignService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 每天4点自动移除已结束区服
 *
 * @author luopeihuan
 */
@Slf4j
@Component
public class CampaignChecker {
    @Autowired
    private IGameCampaignService gameCampaignService;

    @Autowired
    private IOpenServiceCampaignService openServiceCampaignService;

    @Async
    // 秒 分 时 日 月 星期 年份
    @Scheduled(cron = "0 0 4 * * ?")
    public void removeCompletedServer() {
        log.info("start removeCompletedServer");
        long start = System.currentTimeMillis();
        List<GameCampaign> campaignList = gameCampaignService.list();
        for (GameCampaign campaign : campaignList) {
            log.info("start removeCompletedServer, campaignId:{}", campaign.getId());
            Result<?> result = gameCampaignService.removeCompletedServer(campaign);
            log.info("finish removeCompletedServer, campaignId:{}, result:{}", campaign.getId(), result);
        }

        // 只能移除本服的开服活动
        List<OpenServiceCampaign> openServiceCampaigns = openServiceCampaignService.list(
                Wrappers.<OpenServiceCampaign>lambdaQuery().eq(OpenServiceCampaign::getCross, 0)
        );
        for (OpenServiceCampaign campaign : openServiceCampaigns) {
            log.info("start removeCompletedServer, open service campaignId:{}", campaign.getId());
            Result<?> result = openServiceCampaignService.removeCompletedServer(campaign);
            log.info("finish removeCompletedServer, open service campaignId:{}, result:{}", campaign.getId(), result);
        }
        log.info("finish removeCompletedServer, cost:{}ms", (System.currentTimeMillis() - start));
    }
}
