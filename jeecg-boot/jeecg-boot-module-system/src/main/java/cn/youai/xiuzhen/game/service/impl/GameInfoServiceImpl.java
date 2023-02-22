package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.game.entity.GameInfo;
import cn.youai.xiuzhen.game.entity.GameReview;
import cn.youai.xiuzhen.game.mapper.GameInfoMapper;
import cn.youai.xiuzhen.game.model.GameClientConfig;
import cn.youai.xiuzhen.game.model.GameReviewConfig;
import cn.youai.xiuzhen.game.service.IGameInfoService;
import cn.youai.xiuzhen.game.service.IGameReviewService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.dreamlu.mica.core.utils.$;
import org.jeecg.JsonFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏信息
 * @date 2019-12-11
 */
@Service
@DS("master")
public class GameInfoServiceImpl extends ServiceImpl<GameInfoMapper, GameInfo> implements IGameInfoService {

    @Autowired
    private IGameReviewService reviewService;

    @Value("${app.folder.game:}")
    private String gameFolder;

    @Value("${app.folder.frontend:}")
    private String frontendFolder;

    @Override
    public void refreshConfig() {
        List<GameInfo> gameInfoList = list();
        for (GameInfo gameInfo : gameInfoList) {
            GameClientConfig gameClientConfig = new GameClientConfig();
            List<GameReview> reviewList = reviewService.list(Wrappers.<GameReview>lambdaQuery().eq(GameReview::getGameId, gameInfo.getId()).eq(GameReview::getStatus, 1));
            if (CollUtil.isNotEmpty(reviewList)) {
                List<GameReviewConfig> list = new ArrayList<>();
                for (GameReview it : reviewList) {
                    GameReviewConfig reviewConfig = new GameReviewConfig();
                    $.copy(it, reviewConfig);
                    list.add(reviewConfig);
                }
                gameClientConfig.setReviews(list);
            }
            $.copy(gameInfo, gameClientConfig);
            JsonFileUtils.writeJsonFile(gameClientConfig, gameFolder, gameInfo.getYaSimpleName());
        }

        // 替换前端部署页面的 config.json
        if (StringUtils.isNotEmpty(frontendFolder) && FileUtil.exist(frontendFolder)) {
            final List<File> subDirList = Arrays.stream(Objects.requireNonNull(new File(frontendFolder).listFiles())).filter(File::isDirectory).collect(Collectors.toList());
            for (File dir : subDirList) {
                File revisionFile = FileUtil.file(dir, "revision.txt");
                if (!FileUtil.exist(revisionFile)) {
                    continue;
                }

                // 新版配置文件名 config.json，旧版本 versionInfo.json
                File configFile = FileUtil.file(dir, "config.json");
                if (!FileUtil.exist(configFile)) {
                    configFile = FileUtil.file(dir, "versionInfo.json");
                }

                if (!FileUtil.exist(configFile)) {
                    continue;
                }

                String fileData = FileUtil.readUtf8String(configFile);
                JSONObject configJson = JSON.parseObject(fileData);
                String configUrl = configJson.getString("configUrl");
                if (StringUtils.isNotEmpty(configUrl)) {
                    String simpleName = FileNameUtil.getName(configUrl);
                    File gameConfigFile = FileUtil.file(new File(gameFolder), simpleName);
                    if (FileUtil.exist(gameConfigFile)) {
                        String gameConfig = FileUtil.readUtf8String(gameConfigFile);
                        if (StringUtils.isNotEmpty(gameConfig)) {
                            configJson.put("gameConfig", JSON.parseObject(gameConfig));
                        }
                    }
                }
                // 更新配置
                FileUtil.writeUtf8String(JSON.toJSONString(configJson), configFile);
            }
        }
    }
}
