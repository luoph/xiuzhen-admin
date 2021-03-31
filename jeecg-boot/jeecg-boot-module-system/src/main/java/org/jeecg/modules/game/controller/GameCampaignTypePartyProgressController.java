package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameCampaignTypePartyProgress;
import org.jeecg.modules.game.service.IGameCampaignTypePartyProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日派对进度任务
 * @date 2021-03-30
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignTypePartyProgress")
public class GameCampaignTypePartyProgressController extends JeecgController<GameCampaignTypePartyProgress, IGameCampaignTypePartyProgressService> {

	@Autowired
	private IGameCampaignTypePartyProgressService gameCampaignTypePartyProgressService;
	
	/**
	 * 分页列表查询
	 *
	 * @param gameCampaignTypePartyProgress 数据实体
	 * @param pageNo 页码
	 * @param pageSize 分页大小
	 * @param req 请求
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "节日派对进度任务-列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GameCampaignTypePartyProgress gameCampaignTypePartyProgress,
								   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<GameCampaignTypePartyProgress> queryWrapper = QueryGenerator.initQueryWrapper(gameCampaignTypePartyProgress, req.getParameterMap());
		Page<GameCampaignTypePartyProgress> page = new Page<>(pageNo, pageSize);
		IPage<GameCampaignTypePartyProgress> pageList = gameCampaignTypePartyProgressService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param gameCampaignTypePartyProgress 数据实体
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "节日派对进度任务-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody GameCampaignTypePartyProgress gameCampaignTypePartyProgress) {
		gameCampaignTypePartyProgressService.save(gameCampaignTypePartyProgress);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param gameCampaignTypePartyProgress 数据实体
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "节日派对进度任务-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody GameCampaignTypePartyProgress gameCampaignTypePartyProgress) {
		gameCampaignTypePartyProgressService.updateById(gameCampaignTypePartyProgress);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id 实体id
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "节日派对进度任务-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id") String id) {
		gameCampaignTypePartyProgressService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids id列表，使用','分割的字符串
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "节日派对进度任务-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
		this.gameCampaignTypePartyProgressService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id 实体id
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "节日派对进度任务-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id") String id) {
		GameCampaignTypePartyProgress gameCampaignTypePartyProgress = gameCampaignTypePartyProgressService.getById(id);
		if(gameCampaignTypePartyProgress == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(gameCampaignTypePartyProgress);
	}

    /**
     * 导出excel
     *
   * @param request 请求
   * @param gameCampaignTypePartyProgress 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypePartyProgress gameCampaignTypePartyProgress) {
        return super.exportXls(request, gameCampaignTypePartyProgress, GameCampaignTypePartyProgress.class, "节日派对进度任务");
    }

    /**
     * 通过excel导入数据
     *
   * @param request 请求
   * @param response 响应
   * @return {@linkplain Result}
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignTypePartyProgress.class);
    }

}
