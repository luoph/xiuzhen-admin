package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.GameVip;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 敏感词
 * @date 2023-01-10
 */
public interface GameVipMapper extends BaseMapper<GameVip> {
    IPage<GameVip> queryVipList(Page<?> page, @Param("entity") GameVip entity);

}
