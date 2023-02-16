package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameReview;
import cn.youai.xiuzhen.game.mapper.GameReviewMapper;
import cn.youai.xiuzhen.game.service.IGameReviewService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏审核信息
 * @date 2023-02-16
 */
@Service
@DS("master")
public class GameReviewServiceImpl extends ServiceImpl<GameReviewMapper, GameReview> implements IGameReviewService {

}
