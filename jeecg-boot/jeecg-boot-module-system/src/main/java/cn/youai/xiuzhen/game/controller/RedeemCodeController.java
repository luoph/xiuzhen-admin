package cn.youai.xiuzhen.game.controller;

import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.game.entity.GameRedeemCode;
import cn.youai.xiuzhen.game.service.IGameRedeemCodeService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.RandomStringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 激活码
 * @date 2020-01-07
 */
@Slf4j
@RestController
@RequestMapping("game/redeemCode")
public class RedeemCodeController extends JeecgController<GameRedeemCode, IGameRedeemCodeService> {

    @AutoLog(value = "激活码-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameRedeemCode entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "激活码-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameRedeemCode entity) {
        HashSet<String> existsCodes = new HashSet<>(service.selectExistsRedeemCode());
        if (entity.getBatchNum() != null && entity.getBatchNum() > 0) {
            // 1.批量自动生成激活码
            List<GameRedeemCode> gameRedeemCodes = new ArrayList<>(entity.getBatchNum());
            for (int i = 0; i < entity.getBatchNum(); i++) {
                // 自动生产激活码
                int index = 0;
                String code = RandomStringUtils.genCdKeyCode();
                // 如果重复，继续随机生成，并限定循环不超过10次
                while (existsCodes.contains(code) && index++ < 10) {
                    code = RandomStringUtils.genCdKeyCode();
                }

                if (!existsCodes.contains(code)) {
                    existsCodes.add(code);
                    gameRedeemCodes.add(new GameRedeemCode(entity.getActivityId(), code, entity.getTotalNum(), 0, entity.getStatus()));
                }
            }
            service.saveBatch(gameRedeemCodes, gameRedeemCodes.size());
        } else if (entity.getCode() != null && entity.getCode().contains(",")) {
            // 2.批量激活码字符串生成
            Set<String> codes = StringUtils.split2Set(entity.getCode());
            List<GameRedeemCode> gameRedeemCodes = new ArrayList<>(codes.size());
            for (String code : codes) {
                // 自动生产激活码
                if (!existsCodes.contains(code)) {
                    gameRedeemCodes.add(new GameRedeemCode(entity.getActivityId(), code, entity.getTotalNum(), 0, entity.getStatus()));
                }
            }
            service.saveBatch(gameRedeemCodes, gameRedeemCodes.size());
        } else {
            // 3.单个添加
            if (!existsCodes.contains(entity.getCode())) {
                service.save(entity);
            }
        }
        return Result.ok("添加成功！");
    }

    @AutoLog(value = "激活码-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameRedeemCode entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "激活码-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "激活码-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "激活码-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "激活码-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameRedeemCode entity) {
        return super.exportXls(request, entity, GameRedeemCode.class, "激活码");
    }

    @AutoLog(value = "激活码-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameRedeemCode.class);
    }

}
