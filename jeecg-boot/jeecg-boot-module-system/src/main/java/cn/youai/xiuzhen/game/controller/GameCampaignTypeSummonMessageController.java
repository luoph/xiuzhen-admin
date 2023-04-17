package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaign;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeSummonMessage;
import cn.youai.xiuzhen.game.service.IGameCampaignService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeSummonMessageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 节日活动-召唤活动-传闻
 * @Author: jeecg-boot
 * @Date:   2023-04-12
 * @Version: V1.0
 */
@Api(tags="节日活动-召唤活动-传闻")
@RestController
@RequestMapping("/game/gameCampaignTypeSummonMessage")
@Slf4j
public class GameCampaignTypeSummonMessageController extends JeecgController<GameCampaignTypeSummonMessage, IGameCampaignTypeSummonMessageService> {
	@Autowired
	private IGameCampaignTypeSummonMessageService gameCampaignTypeSummonMessageService;

	 @Autowired
	 private IGameCampaignService gameCampaignService;

	 @Autowired
	 private IGameCampaignTypeService gameCampaignTypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param gameCampaignTypeSummonMessage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "节日活动-召唤活动-传闻-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<GameCampaignTypeSummonMessage>> queryPageList(GameCampaignTypeSummonMessage gameCampaignTypeSummonMessage,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<GameCampaignTypeSummonMessage> queryWrapper = QueryGenerator.initQueryWrapper(gameCampaignTypeSummonMessage, req.getParameterMap());
		Page<GameCampaignTypeSummonMessage> page = new Page<GameCampaignTypeSummonMessage>(pageNo, pageSize);
		IPage<GameCampaignTypeSummonMessage> pageList = gameCampaignTypeSummonMessageService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param gameCampaignTypeSummonMessage
	 * @return
	 */
	@AutoLog(value = "节日活动-召唤活动-传闻-添加")
	//@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_summon_message:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody GameCampaignTypeSummonMessage gameCampaignTypeSummonMessage) {
		gameCampaignTypeSummonMessageService.save(gameCampaignTypeSummonMessage);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param gameCampaignTypeSummonMessage
	 * @return
	 */
	@AutoLog(value = "节日活动-召唤活动-传闻-编辑")
	//@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_summon_message:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody GameCampaignTypeSummonMessage gameCampaignTypeSummonMessage) {
		gameCampaignTypeSummonMessageService.updateById(gameCampaignTypeSummonMessage);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "节日活动-召唤活动-传闻-通过id删除")
	//@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_summon_message:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		gameCampaignTypeSummonMessageService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "节日活动-召唤活动-传闻-批量删除")
	//@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_summon_message:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.gameCampaignTypeSummonMessageService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "节日活动-召唤活动-传闻-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<GameCampaignTypeSummonMessage> queryById(@RequestParam(name="id",required=true) String id) {
		GameCampaignTypeSummonMessage gameCampaignTypeSummonMessage = gameCampaignTypeSummonMessageService.getById(id);
		if(gameCampaignTypeSummonMessage==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(gameCampaignTypeSummonMessage);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param gameCampaignTypeSummonMessage
    */
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_type_summon_message:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeSummonMessage gameCampaignTypeSummonMessage) {
        return super.exportXls(request, gameCampaignTypeSummonMessage, GameCampaignTypeSummonMessage.class, CampaignType.SUMMON.getName() + "-传闻");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("game_campaign_type_summon_message:importExcel")
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response,
								 @RequestParam(name = "campaignId") Long campaignId, @RequestParam(name = "typeId") Long typeId) {
		GameCampaign gameCampaign = gameCampaignService.getById(campaignId);
		if (null == gameCampaign) {
			return Result.error("找不到主活动配置");
		}
		return gameCampaignTypeService.importExcel(gameCampaign, typeId, request, CampaignType.SUMMON.getName() + "-传闻", service.getClass());
	}

}
