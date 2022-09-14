package org.jeecg.modules.game.controller;

import cn.youai.basics.model.DataResponse;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.game.entity.GamePlayerBanInfo;
import org.jeecg.modules.game.service.IPlayerBanInfoService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 封禁管理
 * @date 2020-10-22
 */
@Slf4j
@RestController
@RequestMapping("player/playerBanInfo")
public class PlayerBanInfoController extends JeecgController<GamePlayerBanInfo, IPlayerBanInfoService> {

    private static final Type RESPONSE_ONLINE_NUM = new TypeReference<DataResponse<Integer>>() {
    }.getType();

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @param req      请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "封禁管理-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GamePlayerBanInfo entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        IPage<GamePlayerBanInfo> pageList = pageList(entity, pageNo, pageSize, req);
        List<GamePlayerBanInfo> records = pageList.getRecords();
        for (GamePlayerBanInfo record : records) {
            if (record.getIpBan() == null && record.getPlayerIdBan() == null && record.getIdentifierBan() == null) {
                record.setAllBan(true);
            } else {
                record.setAllBan(false);
            }
        }
        pageList.setRecords(records);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param entity 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "封禁管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GamePlayerBanInfo entity) {
        Date date = new Date();
        entity.setCreateDate(date);
        entity.setCreateTime(date);
        entity.setUpdateTime(date);
        // 获取当前操作的用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        entity.setOperator(sysUser.getUsername());
        return super.add(entity);
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "封禁管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

}
