package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.JsonFileUtils;
import org.jeecg.modules.game.entity.GameAppUpdate;
import org.jeecg.modules.game.mapper.GameAppUpdateMapper;
import org.jeecg.modules.game.model.AppConfig;
import org.jeecg.modules.game.service.IGameAppUpdateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 客户端更新配置
 * @date 2021-06-10
 */
@Service
public class GameAppUpdateServiceImpl extends ServiceImpl<GameAppUpdateMapper, GameAppUpdate> implements IGameAppUpdateService {

    @Value("${app.folder.update}")
    private String updateFolder;

    @Override
    public void updateConfig() {
        List<GameAppUpdate> list = list();
        Map<String, List<GameAppUpdate>> map = new HashMap<>();
        if (CollUtil.isNotEmpty(list)) {
            // 按照包名进行分组
            for (GameAppUpdate entity : list) {
                List<GameAppUpdate> configs = map.computeIfAbsent(packageKey(entity), k -> new ArrayList<>());
                configs.add(entity);
            }
        }

        Map<String, List<AppConfig>> configMap = new HashMap<>();
        // 包名内，按照平台，仅保留一个版本号最大的配置
        for (String packageKey : map.keySet()) {
            List<AppConfig> configs = configMap.computeIfAbsent(packageKey, k -> new ArrayList<>());

            Map<String, List<GameAppUpdate>> platformMap = new HashMap<>();
            List<GameAppUpdate> appUpdates = map.get(packageKey);

            // 按照平台，仅保留一个版本号最大的配置
            for (GameAppUpdate entity : appUpdates) {
                List<GameAppUpdate> platformList = platformMap.computeIfAbsent(entity.getPlatform(), k -> new ArrayList<>());
                platformList.add(entity);
            }

            platformMap.forEach((k, v) -> v.stream().max(Comparator.comparing(GameAppUpdate::getVersionCode)).ifPresent(t -> configs.add(AppConfig.valueOf(t))));
        }

        configMap.forEach((k, v) -> JsonFileUtils.writeJsonFile(v, updateFolder, k));
    }

    private String packageKey(GameAppUpdate entity) {
        return entity.getPackageName() + "_" + entity.getChannel();
    }
}
