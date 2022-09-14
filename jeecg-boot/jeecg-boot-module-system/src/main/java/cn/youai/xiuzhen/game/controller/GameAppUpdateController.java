package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GameAppUpdate;
import cn.youai.xiuzhen.game.service.IGameAppUpdateService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 客户端版本
 * @date 2021-06-10
 */
@Slf4j
@RestController
@RequestMapping("game/gameAppUpdate")
public class GameAppUpdateController extends JeecgController<GameAppUpdate, IGameAppUpdateService> {

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @param req      请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "客户端版本-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameAppUpdate entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    /**
     * 添加
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "客户端版本-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameAppUpdate entity) {
        return super.add(entity);
    }

    /**
     * 编辑
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "客户端版本-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameAppUpdate entity) {
        return super.edit(entity);
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "客户端版本-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "客户端版本-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "客户端版本-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    /**
     * 导出excel
     *
     * @param request       请求
     * @param gameAppUpdate 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameAppUpdate gameAppUpdate) {
        return super.exportXls(request, gameAppUpdate, GameAppUpdate.class, "客户端版本");
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
        return super.importExcel(request, response, GameAppUpdate.class);
    }

    @AutoLog(value = "客户端版本-刷新版本配置")
    @ApiOperation(value = "客户端版本-刷新版本配置", notes = "客户端版本-刷新版本配置")
    @GetMapping(value = "/updateConfig")
    public Result<?> updateConfig(HttpServletRequest req) {
        try {
            service.updateConfig();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.ok("刷新成功");
    }
}
