package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.game.entity.GameUpgradeNotice;
import cn.youai.xiuzhen.game.mapper.GameUpgradeNoticeMapper;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.game.service.IGameUpgradeNoticeService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 更新公告
 * @date 2021-03-02
 */
@Slf4j
@Service
@DS("master")
public class GameUpgradeNoticeServiceImpl extends ServiceImpl<GameUpgradeNoticeMapper, GameUpgradeNotice> implements IGameUpgradeNoticeService {

    @Value("${app.campaign-update-url:/campaign/reload}")
    private String campaignUpdateUrl;

    @Autowired
    private IGameServerService gameServerService;

    @Override
    public void syncServerAll(GameUpgradeNotice gameUpgradeNotice) {
        Set<Integer> currentIds = new HashSet<>(StringUtils.split2Int(gameUpgradeNotice.getServerIds()));
        Set<Integer> allIds = new HashSet<>(StringUtils.split2Int(gameUpgradeNotice.getLastServerIds()));
        allIds.addAll(currentIds);

        Map<String, Object> params = new HashMap<>(allIds.size());
        params.put("id", gameUpgradeNotice.getId());
        params.put("name", "GameUpgradeNotice");

        Map<Integer, Response> response = gameServerService.getUrl(allIds, campaignUpdateUrl, params);
        log.info("sync id:{} response:{}", gameUpgradeNotice.getId(), response);

        // 更新已刷新的服务器id
        gameUpgradeNotice.setLastServerIds(StrUtil.join(",", currentIds));

        GameUpgradeNotice entity = new GameUpgradeNotice();
        entity.setId(gameUpgradeNotice.getId());
        entity.setLastServerIds(gameUpgradeNotice.getLastServerIds());
        updateById(entity);
    }
}
