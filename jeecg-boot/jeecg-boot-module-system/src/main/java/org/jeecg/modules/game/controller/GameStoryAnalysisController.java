package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.game.entity.GameStoryAnalysis;
import org.jeecg.modules.game.entity.GameStoryAnalysisVO;
import org.jeecg.modules.game.service.IGameStoryAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 剧情分析
 * @date 2021-03-26
 */
@Slf4j
@RestController
@RequestMapping("game/gameStoryAnalysis")
public class GameStoryAnalysisController {

	@Autowired
	private IGameStoryAnalysisService gameStoryAnalysisService;

	/**
	 * 分页列表查询
	 *
	 * @param gameStoryAnalysisVO 数据实体
	 * @param pageNo              页码
	 * @param pageSize            分页大小
	 * @param req                 请求
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "剧情分析-列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GameStoryAnalysisVO gameStoryAnalysisVO,
								   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
								   HttpServletRequest req) {
		if (gameStoryAnalysisVO.getServerId() == null || gameStoryAnalysisVO.getServerId() <= 0) {
			return Result.error("请选择服务器！");
		}

		if (gameStoryAnalysisVO.getAnalysisDate() == null) {
			return Result.error("请选择分析日期！");
		}
		IPage<GameStoryAnalysisVO> gameStoryAnalysisVOIPage = gameStoryAnalysisService.queryGameStoryAnalysisList(gameStoryAnalysisVO, pageSize, pageNo);
		if (gameStoryAnalysisVOIPage != null) {
			return Result.ok(gameStoryAnalysisVOIPage);
		}
		return Result.ok();
	}

	/**
	 * 添加
	 *
	 * @param gameStoryAnalysis 数据实体
	 * @return {@linkplain Result}
	 */
//	@AutoLog(value = "剧情分析-添加")
//	@PostMapping(value = "/add")
//	public Result<?> add(@RequestBody GameStoryAnalysis gameStoryAnalysis) {
//		gameStoryAnalysisService.save(gameStoryAnalysis);
//		return Result.ok("添加成功！");
//	}

	/**
	 * 编辑
	 *
	 * @param gameStoryAnalysis 数据实体
	 * @return {@linkplain Result}
	 */
//	@AutoLog(value = "剧情分析-编辑")
//	@PutMapping(value = "/edit")
//	public Result<?> edit(@RequestBody GameStoryAnalysis gameStoryAnalysis) {
//		gameStoryAnalysisService.updateById(gameStoryAnalysis);
//		return Result.ok("编辑成功!");
//	}

	/**
	 * 通过id删除
	 *
	 * @param id 实体id
	 * @return {@linkplain Result}
	 */
//	@AutoLog(value = "剧情分析-通过id删除")
//	@DeleteMapping(value = "/delete")
//	public Result<?> delete(@RequestParam(name = "id") String id) {
//		gameStoryAnalysisService.removeById(id);
//		return Result.ok("删除成功!");
//	}

	/**
	 *  批量删除
	 *
	 * @param ids id列表，使用','分割的字符串
	 * @return {@linkplain Result}
	 */
//	@AutoLog(value = "剧情分析-批量删除")
//	@DeleteMapping(value = "/deleteBatch")
//	public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
//		this.gameStoryAnalysisService.removeByIds(Arrays.asList(ids.split(",")));
//		return Result.ok("批量删除成功！");
//	}

	/**
	 * 通过id查询
	 *
	 * @param id 实体id
	 * @return {@linkplain Result}
	 */
//	@AutoLog(value = "剧情分析-通过id查询")
//	@GetMapping(value = "/queryById")
//	public Result<?> queryById(@RequestParam(name = "id") String id) {
//		GameStoryAnalysis gameStoryAnalysis = gameStoryAnalysisService.getById(id);
//		if(gameStoryAnalysis == null) {
//			return Result.error("未找到对应数据");
//		}
//		return Result.ok(gameStoryAnalysis);
//	}

	/**
	 * 导出excel
	 *
	 * @param request           请求
	 * @param gameStoryAnalysis 实体
	 */
//    @RequestMapping(value = "/exportXls")
//    public ModelAndView exportXls(HttpServletRequest request, GameStoryAnalysis gameStoryAnalysis) {
//        return super.exportXls(request, gameStoryAnalysis, GameStoryAnalysis.class, "剧情分析");
//    }

	/**
	 * 通过excel导入数据
	 *
	 * @param request  请求
	 * @param response 响应
	 * @return {@linkplain Result}
	 */
//    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
//    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
//        return super.importExcel(request, response, GameStoryAnalysis.class);
//    }

}
