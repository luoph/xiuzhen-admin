package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.JsonFileUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameNotice;
import org.jeecg.modules.game.model.NoticeConfig;
import org.jeecg.modules.game.service.IGameNoticeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏公告
 * @date 2019-12-13
 */
@Slf4j
@RestController
@Api(tags = "游戏公告")
@RequestMapping("/game/gameNotice")
public class GameNoticeController extends JeecgController<GameNotice, IGameNoticeService> {

    @Value("${app.folder.notice}")
    private String noticeFolder;

    @Autowired
    private IGameNoticeService gameNoticeService;

    /**
     * 分页列表查询
     *
     * @param gameNotice 数据实体
     * @param pageNo     页码
     * @param pageSize   分页大小
     * @param req        请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏公告-列表查询")
    @ApiOperation(value = "游戏公告-列表查询", notes = "游戏公告-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameNotice gameNotice,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameNotice> queryWrapper = QueryGenerator.initQueryWrapper(gameNotice, req.getParameterMap());
        Page<GameNotice> page = new Page<>(pageNo, pageSize);
        IPage<GameNotice> pageList = gameNoticeService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameNotice 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏公告-添加")
    @ApiOperation(value = "游戏公告-添加", notes = "游戏公告-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameNotice gameNotice) {
        gameNotice.setContent(formatNoticeHtml(gameNotice.getContent()));
        gameNoticeService.save(gameNotice);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameNotice 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏公告-编辑")
    @ApiOperation(value = "游戏公告-编辑", notes = "游戏公告-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameNotice gameNotice) {
        gameNotice.setContent(formatNoticeHtml(gameNotice.getContent()));
        gameNoticeService.updateById(gameNotice);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏公告-通过id删除")
    @ApiOperation(value = "游戏公告-通过id删除", notes = "游戏公告-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameNoticeService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏公告-批量删除")
    @ApiOperation(value = "游戏公告-批量删除", notes = "游戏公告-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameNoticeService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏公告-通过id查询")
    @ApiOperation(value = "游戏公告-通过id查询", notes = "游戏公告-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameNotice gameNotice = gameNoticeService.getById(id);
        return Result.ok(gameNotice);
    }

    /**
     * 导出excel
     *
     * @param request    请求
     * @param gameNotice 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameNotice gameNotice) {
        return super.exportXls(request, gameNotice, GameNotice.class, "游戏公告");
    }

    /**
     * 通过excel导入数据
     *
     * @param request  请求
     * @param response 响应
     * @return {@linkplain Result}
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameNotice.class);
    }

    @GetMapping(value = "/updateNoticeConfig")
    public Result<?> updateServerConfig(HttpServletRequest req) {
        try {
            List<GameNotice> noticeList = gameNoticeService.list();
            for (GameNotice gameNotice : noticeList) {
                NoticeConfig notice = new NoticeConfig();
                BeanUtils.copyProperties(gameNotice, notice);
                if (gameNotice.getStatus() == 1) {
                    JsonFileUtils.writeJsonFile(notice, noticeFolder, String.valueOf(gameNotice.getId()));
                } else {
                    JsonFileUtils.deleteJsonFile(noticeFolder, String.valueOf(gameNotice.getId()));
                }
            }
        } catch (Exception e) {
            log.error("updateServerConfig error", e);
            return Result.error(e.getMessage());
        }
        return Result.ok("刷新成功");
    }

    private static String formatNoticeHtml(String input) {
        input = input.replace("</p>\\n<p>", "<br/>");
        input = input.replace("<p>", "");
        input = input.replace("</p>", "");
        return input;
    }

//    public static void main(String[] args) {
//        String text = "<p>亲爱的各位道友：</p>\\n<p>游戏将于<span style=\\\"color: #2dc26b;\\\">12月18日08:00</span>进行版本更新，更新内容如下：</p>\\n<p>一：法宝系统优化</p>\\n<p>1.法宝替换：法宝优化为可替换，获得更好的法宝可直接替换身上法宝增加属性，替换法宝后，<span style=\\\"color: #e03e2d;\\\">精炼/拓星/符文/铸灵/追加等属性将100%继承</span></p>\\n<p>2.法宝套装：法宝增加套装功能，被替换下的旧法宝依旧可以激活套装属性，提升仙力</p>\\n<p>3.法宝合成：增加法宝合成功能，橙色法宝可合成红色法宝，红色法宝可合成粉色法宝，可<span style=\\\"color: #e03e2d;\\\">提升大量人物属性</span>！</p>\\n<p>4.法宝铸灵：法宝铸灵将不再改变法宝颜色品质（属性不变），法宝颜色可通过法宝合成进行提升</p>\\n<p>5.法宝品阶：法宝将会获得品阶属性，品阶越高，基础属性越高，高品的法宝可通过高级的封魔获得</p>\\n<p>&nbsp;</p>\\n<p>二：封魔优化</p>\\n<p>1.封魔玩法将<span style=\\\"color: #e03e2d;\\\">掉落大量法宝</span>跟法宝合成石，掉落法宝可直接穿戴/合成高级法宝增加大量属性</p>\\n<p>2.魔王入侵优化，优化魔王等级提升规则，让魔王成长更艰难，更轻松击败魔王</p>\\n<p>3.魔王入侵奖励优化，<span style=\\\"color: #e03e2d;\\\">提升个人/仙盟排名奖励跟最后一刀奖励</span></p>\\n<p>4.北冥魔海cd优化，缩短为5分钟</p>\\n<p>5.蛇陵魔窟可挑战次数提升</p>\\n<p>6.仙职天魔优化，成为九天仙君，将可以<span style=\\\"color: #e03e2d;\\\">无限次数击杀仙职天魔</span></p>\\n<p>&nbsp;</p>\\n<p>三：便捷性优化</p>\\n<p>1.自动功能：<span style=\\\"color: #e03e2d;\\\">彻底解放双手</span>，自动功能包括自动挑战封魔、符文秘境自动下一层、剧情自动下一章（自动功能需停留在对应玩法界面哦）</p>\\n<p>2.扫荡功能：已通关秘境，将可以<span style=\\\"color: #e03e2d;\\\">免费扫荡</span>获得100%奖励</p>\\n<p>3.一键功能：新增法宝<span style=\\\"color: #e03e2d;\\\">一键精炼</span>、一键拓星、圣灵一键升级</p>\\n<p>&nbsp;</p>\\n<p>四：渡劫系统优化</p>\\n<p>1.天劫系统：新增天劫系统，达到突破境界时，需要完成渡天劫任务才可突破境界，且可<span style=\\\"color: #e03e2d;\\\">大幅提升主角属性</span></p>\\n<p>2.飞升系统：飞升系统任务优化，增加任务完成奖励</p>\\n<p>&nbsp;</p>\\n<p>五：新增仙盟技能</p>\\n<p>1.新增仙盟技能，通过仙盟入口进入（需要加入仙盟才可开启此功能）</p>\\n<p>2.仙盟技能可通过<span style=\\\"color: #e03e2d;\\\">消耗仙贡提升属性</span>等级，获得属性加成</p>\\n<p>3.通过提升仙盟等级和主角境界可提升仙盟技能等级上限</p>\\n<p>&nbsp;</p>\\n<p>六：双修系统优化</p>\\n<p>1.优化双修界面展示跟交互，更加清晰方便的操作</p>\\n<p>2.提升每日可双修次数</p>\\n<p>3.提升信物强化属性</p>\\n<p>&nbsp;</p>\\n<p>七：属性/仙力优化</p>\\n<p>1.圣灵/法宝/飞剑等系统属性调整，将大幅提升基础属性，调整百分比属性（<span style=\\\"color: #e03e2d;\\\">仙力将会大幅提升</span>）</p>\\n<p>2.优化命中/闪避/卓越/会心属性仙力，提升战斗体验</p>\\n<p>&nbsp;</p>\\n<p>八：合成优化</p>\\n<p>1.下调圣灵合成碎片，合成圣灵将会更加轻松</p>\\n<p>2.调整时装合成碎片，通过登录/月卡/活动等方式均可获得时装碎片</p>\\n<p>&nbsp;</p>\\n<p>九：玩法产出优化</p>\\n<p>1.福地夺宝、上古遗迹、修罗古域等玩法产出提升</p>\\n<p>2.境界奖励、签到奖励、道果仙树等功能产出提升</p>\\n<p>&nbsp;</p>\\n<p>十：仙职优化</p>\\n<p>1.仙职奖励提升，八荒仙君额外赠送<span style=\\\"color: #e03e2d;\\\">仙兽-烛龙</span>，九天帝君额外赠送<span style=\\\"color: #e03e2d;\\\">仙兽-九色仙鹿</span></p>\\n<p>2.仙职特权优化，八荒仙君将增加秘境/封魔次数购买特权，不再享有修为加成特权，九天帝君将享有专属仙职天魔，修为加成特权，不再享有次数购买特权</p>\\n<p>3.仙职购买方式改为花费仙玉购买，两种仙职可同时任职</p>\\n<p>4.<span style=\\\"color: #e03e2d;\\\">更新前已经购买仙职的玩家，将会100%返还等额的仙玉，并补发新的仙职奖励，再额外赠送20%的仙玉补偿</span></p>\\n<p>&nbsp;</p>\\n<p>十一：月卡优化</p>\\n<p>1.月卡每日赠送奖励优化，更新后次日可领取新奖励（<span style=\\\"color: #e03e2d;\\\">新奖励将大幅提升</span>）</p>\\n<p>2.月卡理解活动奖励优化为返还等额仙玉</p>\\n<p>3.<span style=\\\"color: #e03e2d;\\\">更新前已经购买月卡的玩家，将会100%返还等额的仙玉，再额外赠送20%的仙玉补偿</span></p>\\n<p>&nbsp;</p>\\n<p>十二：天道令优化</p>\\n<p>1.天道令将优化等级、任务、奖励（<span style=\\\"color: #e03e2d;\\\">新版奖励将大幅提升</span>）</p>\\n<p>2.更新后玩家的天道令等级跟奖励将会重置，重新完成任务即可提升等级</p>\\n<p>3.<span style=\\\"color: #e03e2d;\\\">更新前已经购买天道令的玩家，将会100%返还等额的仙玉，再额外赠送20%的仙玉补偿</span></p>\\n<p>&nbsp;</p>\\n<p>十三：首充优化</p>\\n<p>1.首充奖励优化，<span style=\\\"color: #e03e2d;\\\">奖励将大幅提升</span></p>\\n<p>2.<span style=\\\"color: #e03e2d;\\\">更新前已经领取首充奖励的玩家，将会补发新的首充奖励</span></p>\\n<p>&nbsp;</p>\\n<p>十四：时装优化</p>\\n<p>1.时装将分为人、灵、仙、圣四个等级，等级越高，属性越高，<span style=\\\"color: #e03e2d;\\\">已激活对应时装的玩家仙力将会得到大幅提升</span></p>\\n<p>2.坊市将下架时装坊市，时装将会通过登录活动/天道令/后续活动中获得</p>\\n<p>3.时装合成调整，取消面具/聊天/名帖碎片，时装可通过人/灵/仙/圣级时装碎片合成获</p>\\n<p>4.<span style=\\\"color: #e03e2d;\\\">旧版本的面具/聊天/名帖碎片将按照时装坊市6绑玉价格回收，玩家更新后在储物袋出售即可</span></p>\\n<p>&nbsp;</p>\\n<p>十五：玩法预告优化</p>\\n<p>1.限时玩法开启时，将通过主界面弹窗提醒玩家参与，再也不怕错过活动了</p>\\n<p>&nbsp;</p>\\n<p>十六：日常优化</p>\\n<p>1.优化日常阅历值条件及奖励</p>\\n<p>2.丰富日常活动任务类型</p>\\n<p>3.提升道果仙树升级属性，仙树等级奖励</p>\\n<p>&nbsp;</p>\\n<p><strong>【开服礼包】</strong></p>\\n<p>登录游戏打开设置-输入xslb888，即可领取专属新手礼包！</p>\\n<p>&nbsp;</p>\\n<p><strong>【联系方式】</strong></p>\\n<p>微信搜索公众号：乘龙互娱，即可联系在线客服。还可领取关注礼包哦！</p>\\n<p>&nbsp;</p>";
//        String out = formatNoticeHtml(text);
//        System.out.println(out);
//    }

}
