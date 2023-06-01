package org.jeecg.common.util.dynamic.db;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * DBHelper
 *
 * @author ocean
 * @since 202306051002
 */
public final class DBHelper {

    private static final Logger log = LoggerFactory.getLogger(cn.youai.server.utils.DBHelper.class);

    private DBHelper() {
    }

    public static final boolean saveBatch(Collection<?> entityList, Class<? extends IService> serviceClazz) {
        return CollUtil.isEmpty(entityList) || ((IService) SpringContextUtils.getBean(serviceClazz)).saveBatch(entityList);
    }

    public static final boolean saveOrUpdateBatch(Collection<?> entityList, Class<? extends IService> serviceClazz) {
        return CollUtil.isEmpty(entityList) || ((IService) SpringContextUtils.getBean(serviceClazz)).saveOrUpdateBatch(entityList);
    }

    public static final boolean updateBatchById(Collection<?> entityList, Class<? extends IService> serviceClazz) {
        return CollUtil.isEmpty(entityList) || ((IService) SpringContextUtils.getBean(serviceClazz)).updateBatchById(entityList);
    }
}
