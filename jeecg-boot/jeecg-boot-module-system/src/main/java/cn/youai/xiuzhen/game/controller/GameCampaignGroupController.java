package cn.youai.xiuzhen.game.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import cn.youai.xiuzhen.game.entity.GameCampaignGroup;
import cn.youai.xiuzhen.game.service.IGameCampaignGroupService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 节日活动分组
 * @Author: jeecg-boot
 * @Date:   2023-07-20
 * @Version: V1.0
 */
@Api(tags="节日活动分组")
@RestController
@RequestMapping("/game/gameCampaignGroup")
@Slf4j
public class GameCampaignGroupController extends JeecgController<GameCampaignGroup, IGameCampaignGroupService> {
	@Autowired
	private IGameCampaignGroupService gameCampaignGroupService;
	
	/**
	 * 分页列表查询
	 *
	 * @param gameCampaignGroup
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "节日活动分组-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<GameCampaignGroup>> queryPageList(GameCampaignGroup gameCampaignGroup,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<GameCampaignGroup> queryWrapper = QueryGenerator.initQueryWrapper(gameCampaignGroup, req.getParameterMap());
		Page<GameCampaignGroup> page = new Page<GameCampaignGroup>(pageNo, pageSize);
		IPage<GameCampaignGroup> pageList = gameCampaignGroupService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param gameCampaignGroup
	 * @return
	 */
	@AutoLog(value = "节日活动分组-添加")
	//@RequiresPermissions("cn.youai.xiuzhen:game_campaign_group:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody GameCampaignGroup gameCampaignGroup) {
		gameCampaignGroupService.save(gameCampaignGroup);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param gameCampaignGroup
	 * @return
	 */
	@AutoLog(value = "节日活动分组-编辑")
	//@RequiresPermissions("cn.youai.xiuzhen:game_campaign_group:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody GameCampaignGroup gameCampaignGroup) {
		gameCampaignGroupService.updateById(gameCampaignGroup);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "节日活动分组-通过id删除")
	//@RequiresPermissions("cn.youai.xiuzhen:game_campaign_group:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		gameCampaignGroupService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "节日活动分组-批量删除")
	//@RequiresPermissions("cn.youai.xiuzhen:game_campaign_group:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.gameCampaignGroupService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "节日活动分组-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<GameCampaignGroup> queryById(@RequestParam(name="id",required=true) String id) {
		GameCampaignGroup gameCampaignGroup = gameCampaignGroupService.getById(id);
		if(gameCampaignGroup==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(gameCampaignGroup);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param gameCampaignGroup
    */
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_group:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignGroup gameCampaignGroup) {
        return super.exportXls(request, gameCampaignGroup, GameCampaignGroup.class, "节日活动分组");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("game_campaign_group:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignGroup.class);
    }

}
