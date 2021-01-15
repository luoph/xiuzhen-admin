package org.jeecg.modules.game.controller;

import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.entity.pojo.DateRange;
import cn.youai.xiuzhen.utils.DateUtils;
import com.alibaba.druid.util.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.game.entity.GameChannel;
import org.jeecg.modules.game.entity.GameRankListVO;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IGameRankListService;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author huli
 * @Description: GameRankListController
 * @date 2021/1/15 10:36
 */

@RestController
@RequestMapping("game/gameRankList")
public class GameRankListController {

    @Value("${app.log.db.table_log_player}")
    String logPlayerTable;

    @Resource
    IGameRankListService gameRankListService;
    /**
     * 查询排行榜列表
     */
    @RequestMapping("/list")
    public Result<?> list(GameRankListVO gameRankListVO,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) throws UnsupportedEncodingException {

        // 服务器空校验
        if (null == gameRankListVO.getServerId()) {
            return Result.error("请选择服务器！");
        }
        // 日期空校验
        if (null == gameRankListVO.getDate()) {
            return Result.error("请选择日期！");
        }

        Page<GameRankListVO> pageVo = new Page<>(pageNo, pageSize);
        List<GameRankListVO> list = gameRankListService.selectGameRankList(logPlayerTable, gameRankListVO);
        pageVo.setRecords(list).setTotal(list.size());
        return Result.ok(pageVo);
    }

}