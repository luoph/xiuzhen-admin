package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.youai.basics.model.DateRange;
import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.game.entity.GameInfo;
import cn.youai.xiuzhen.game.entity.GameReview;
import cn.youai.xiuzhen.game.entity.GameVip;
import cn.youai.xiuzhen.game.entity.GameVps;
import cn.youai.xiuzhen.game.mapper.GameInfoMapper;
import cn.youai.xiuzhen.game.mapper.GameVpsMapper;
import cn.youai.xiuzhen.game.model.GameClientConfig;
import cn.youai.xiuzhen.game.model.GameReviewConfig;
import cn.youai.xiuzhen.game.service.IGameInfoService;
import cn.youai.xiuzhen.game.service.IGameReviewService;
import cn.youai.xiuzhen.game.service.IGameVipService;
import cn.youai.xiuzhen.game.service.IGameVpsService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * @description 虚拟主机
 * @date 2023-04-08
 */
@Service
@DS("master")
public class GameVpsServiceImpl extends ServiceImpl<GameVpsMapper, GameVps> implements IGameVpsService {

    @Override
    public IPage<GameVps> queryList(Page<GameVps> page, GameVps entity, DateRange createTimeRange) {
        return getBaseMapper().queryList(page, entity, createTimeRange);
    }
}
