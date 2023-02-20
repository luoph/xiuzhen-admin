package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaign;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动(1级)
 * @date 2020-12-21
 */
public interface GameOpenServiceCampaignMapper extends BaseMapper<OpenServiceCampaign> {

    List<OpenServiceCampaignDetail> queryCampaignDetailsFastly(@Param("timeType") int timeType, @Param("autoAddServer") int autoAddServer);

}
