package org.jeecg.modules.game.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.youai.commons.model.Response;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.game.entity.GameUpgradeNotice;
import org.jeecg.modules.game.mapper.GameUpgradeNoticeMapper;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.game.service.IGameUpgradeNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 更新公告
 * @date 2021-03-02
 */
@Service
@Slf4j
public class GameUpgradeNoticeServiceImpl extends ServiceImpl<GameUpgradeNoticeMapper, GameUpgradeNotice> implements IGameUpgradeNoticeService {

    @Value("${app.campaign-update-url:/campaign/reload}")
    private String campaignUpdateUrl;

    @Autowired
    private IGameServerService gameServerService;

    @Override
    public void syncServerAll(GameUpgradeNotice gameUpgradeNotice) {

        List<String> lastIds = StrUtil.splitTrim(gameUpgradeNotice.getLastServerIds(), ",");
        List<String> currentIds = StrUtil.splitTrim(gameUpgradeNotice.getServerIds(), ",");
        Set<String> allIds = new HashSet<>(lastIds);
        allIds.addAll(currentIds);

        Map<String, Object> params = new HashMap<>(allIds.size());
        params.put("id", gameUpgradeNotice.getId());
        params.put("name", "GameUpgradeNotice");

        Map<String, Response> response = gameServerService.gameServerGet(allIds, campaignUpdateUrl, params);

        log.info("sync id:{} response:{}", gameUpgradeNotice.getId(), response);

        // 更新已刷新的服务器id
        Collections.sort(currentIds);
        gameUpgradeNotice.setLastServerIds(StrUtil.join(",", currentIds));
        updateById(new GameUpgradeNotice().setId(gameUpgradeNotice.getId()).setLastServerIds(gameUpgradeNotice.getLastServerIds()));
    }
}
