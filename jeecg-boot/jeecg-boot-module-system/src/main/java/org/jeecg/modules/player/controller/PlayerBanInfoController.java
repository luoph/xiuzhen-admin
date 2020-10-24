package org.jeecg.modules.player.controller;

import cn.youai.commons.model.DataResponse;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.player.entity.PlayerBanInfo;
import org.jeecg.modules.player.service.IPlayerBanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PlayerBanInfoController extends JeecgController<PlayerBanInfo, IPlayerBanInfoService> {

	private static final Type RESPONSE_ONLINE_NUM = new TypeReference<DataResponse<Integer>>() {
	}.getType();

	@Autowired
	private IPlayerBanInfoService playerBanInfoService;

	/**
	 * 分页列表查询
	 *
	 * @param playerBanInfo 数据实体
	 * @param pageNo        页码
	 * @param pageSize      分页大小
	 * @param req           请求
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "封禁管理-列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PlayerBanInfo playerBanInfo,
	                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
	                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
	                               HttpServletRequest req) {
		QueryWrapper<PlayerBanInfo> queryWrapper = QueryGenerator.initQueryWrapper(playerBanInfo, req.getParameterMap());
		Page<PlayerBanInfo> page = new Page<>(pageNo, pageSize);
		IPage<PlayerBanInfo> pageList = playerBanInfoService.page(page, queryWrapper);
		List<PlayerBanInfo> records = pageList.getRecords();
		for (PlayerBanInfo record : records) {
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
	 * @param playerBanInfo 数据实体
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "封禁管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PlayerBanInfo playerBanInfo) {
		Date date = new Date();
		playerBanInfo.setCreateDate(date);
		playerBanInfo.setCreateTime(date);
		playerBanInfo.setUpdateTime(date);
		// 获取当前操作的用户
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		playerBanInfo.setOperator(sysUser.getUsername());
		playerBanInfoService.save(playerBanInfo);
		// DataResponse<Integer> response = JSON.parseObject(OkHttpHelper.get(record.getGmUrl() + onlineNumUrl), RESPONSE_ONLINE_NUM);
		return Result.ok("添加成功！");
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
		playerBanInfoService.removeById(id);
		return Result.ok("解封成功!");
	}

}
