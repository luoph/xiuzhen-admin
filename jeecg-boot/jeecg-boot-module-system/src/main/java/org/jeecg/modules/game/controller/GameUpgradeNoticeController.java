package org.jeecg.modules.game.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameUpgradeNotice;
import org.jeecg.modules.game.service.IGameUpgradeNoticeService;
import org.jeecg.modules.game.util.StrHtmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 更新公告
 * @date 2021-03-02
 */
@Slf4j
@RestController
@RequestMapping("game/gameUpgradeNotice")
public class GameUpgradeNoticeController extends JeecgController<GameUpgradeNotice, IGameUpgradeNoticeService> {

    @Autowired
    private IGameUpgradeNoticeService gameUpgradeNoticeService;

    /**
     * 分页列表查询
     *
     * @param gameUpgradeNotice 数据实体
     * @param pageNo            页码
     * @param pageSize          分页大小
     * @param req               请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "更新公告-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameUpgradeNotice gameUpgradeNotice,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameUpgradeNotice> queryWrapper = QueryGenerator.initQueryWrapper(gameUpgradeNotice, req.getParameterMap());
        Page<GameUpgradeNotice> page = new Page<>(pageNo, pageSize);
        IPage<GameUpgradeNotice> pageList = gameUpgradeNoticeService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param model 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "更新公告-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameUpgradeNotice model) {
        model.setNoticeMsg(StrHtmlUtil.formatNoticeHtml(model.getNoticeMsg()));
        // 排序区服id
        List<String> serverIds = StrUtil.splitTrim(model.getServerIds() != null ? model.getServerIds() : "", ",");
        Collections.sort(serverIds);
        model.setServerIds(StrUtil.join(",", serverIds));
        gameUpgradeNoticeService.save(model);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "更新公告-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameUpgradeNotice entity) {
        entity.setNoticeMsg(StrHtmlUtil.formatNoticeHtml(entity.getNoticeMsg()));
        // 排序区服id
        List<String> serverIds = StrUtil.splitTrim(entity.getServerIds() != null ? entity.getServerIds() : "", ",");
        Collections.sort(serverIds);
        String newServerIds = StrUtil.join(",", serverIds);
        if (StringUtils.equals(newServerIds, entity.getServerIds())) {
            entity.setServerIds(newServerIds);
        }
        gameUpgradeNoticeService.updateById(entity);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "更新公告-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        gameUpgradeNoticeService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "更新公告-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameUpgradeNoticeService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "更新公告-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameUpgradeNotice gameUpgradeNotice = gameUpgradeNoticeService.getById(id);
        if (gameUpgradeNotice == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameUpgradeNotice);
    }

    /**
     * 导出excel
     *
     * @param request           请求
     * @param gameUpgradeNotice 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameUpgradeNotice gameUpgradeNotice) {
        return super.exportXls(request, gameUpgradeNotice, GameUpgradeNotice.class, "更新公告");
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
        return super.importExcel(request, response, GameUpgradeNotice.class);
    }


    @GetMapping(value = "/openOrClose")
    public Result<?> openOrClose(@RequestParam(name = "id") int id) {
        GameUpgradeNotice gameUpgradeNotice = gameUpgradeNoticeService.getById(id);
        if (gameUpgradeNotice == null) {
            return Result.error("消息不存在！");
        }
        // 消息状态1-开启 0-暂定
        int status = gameUpgradeNotice.getStatus();
        if (status == 1) {
            gameUpgradeNotice.setStatus(0);
        } else {
            gameUpgradeNotice.setStatus(1);
        }
        gameUpgradeNoticeService.updateById(gameUpgradeNotice);
        return Result.ok("更新成功！");
    }


    @GetMapping(value = "/serverSync")
    public Result<?> serverSync(@RequestParam(name = "id") int id) {
        GameUpgradeNotice gameUpgradeNotice = gameUpgradeNoticeService.getById(id);
        if (gameUpgradeNotice == null) {
            return Result.error("消息不存在！");
        }

        if (StringUtils.isBlank(gameUpgradeNotice.getServerIds())) {
            return Result.error("请配置活动游戏服务器！");
        }
        gameUpgradeNoticeService.syncServerAll(gameUpgradeNotice);
        return Result.ok("同步成功！");
    }
}