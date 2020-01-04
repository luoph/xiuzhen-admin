package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.okhttp.OkHttpHelper;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameSetting;
import org.jeecg.modules.game.service.IGameSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏设置
 * @date 2020-01-04
 */
@Slf4j
@RestController
@RequestMapping("game/gameSetting")
public class GameSettingController extends JeecgController<GameSetting, IGameSettingService> {

    @Value("${app.url.gamecenter}")
    private String gameCenterUrl;

    @Autowired
    private IGameSettingService gameSettingService;

    /**
     * 分页列表查询
     *
     * @param gameSetting 数据实体
     * @param pageNo      页码
     * @param pageSize    分页大小
     * @param req         请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏设置-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameSetting gameSetting,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameSetting> queryWrapper = QueryGenerator.initQueryWrapper(gameSetting, req.getParameterMap());
        Page<GameSetting> page = new Page<>(pageNo, pageSize);
        IPage<GameSetting> pageList = gameSettingService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param gameSetting 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏设置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameSetting gameSetting) {
        gameSettingService.save(gameSetting);
        notifyGameSetting(gameSetting.getDictKey());
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gameSetting 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏设置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameSetting gameSetting) {
        gameSettingService.updateById(gameSetting);
        notifyGameSetting(gameSetting.getDictKey());
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏设置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        GameSetting gameSetting = gameSettingService.getById(id);
        gameSettingService.removeById(id);
        String key = gameSetting != null ? gameSetting.getDictKey() : null;
        notifyGameSetting(key);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏设置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.gameSettingService.removeByIds(Arrays.asList(ids.split(",")));
        notifyGameSetting(null);
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "游戏设置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameSetting gameSetting = gameSettingService.getById(id);
        if (gameSetting == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameSetting);
    }

    /**
     * 导出excel
     *
     * @param request     请求
     * @param gameSetting 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameSetting gameSetting) {
        return super.exportXls(request, gameSetting, GameSetting.class, "游戏设置");
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
        return super.importExcel(request, response, GameSetting.class);
    }

    private void notifyGameSetting(String key) {
        Map<String, String> params = new HashMap<>();
        if (StringUtils.isNotEmpty(key)) {
            params.put("key", key);
        }
        OkHttpHelper.get(gameCenterUrl + "/setting/update", params);
    }
}
