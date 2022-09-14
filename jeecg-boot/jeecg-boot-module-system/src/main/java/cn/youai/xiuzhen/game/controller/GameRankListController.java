package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GameRankListVO;
import cn.youai.xiuzhen.game.service.IGameRankListService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author huli
 * @Description: GameRankListController
 * @date 2021/1/15 10:36
 */

@RestController
@RequestMapping("game/gameRankList")
public class GameRankListController {

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
        //请选择榜单类型
        if (null == gameRankListVO.getRankListType()) {
            return Result.error("请选择榜单类型！");
        }

        Page<GameRankListVO> pageVo = new Page<>(pageNo, pageSize);
        List<GameRankListVO> list = gameRankListService.selectGameRankList(gameRankListVO);
        pageVo.setRecords(list).setTotal(list.size());
        return Result.ok(pageVo);
    }

}