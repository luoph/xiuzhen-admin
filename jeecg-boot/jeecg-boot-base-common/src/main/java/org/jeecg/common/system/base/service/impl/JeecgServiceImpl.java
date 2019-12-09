package org.jeecg.common.system.base.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecg.common.system.base.service.JeecgService;

/**
 * @author dangzhenghui@163.com
 * @version 1.0
 * @description ServiceImpl基类
 * @date 2019-4-21 8:13
 */
@Slf4j
public class JeecgServiceImpl<M extends BaseMapper<T>, T extends JeecgEntity> extends ServiceImpl<M, T> implements JeecgService<T> {

}
