package org.jeecg.modules.demo.test.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.demo.test.entity.JeecgDemo;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description jeecg 测试demo
 * @date 2018-12-29
 */
public interface IJeecgDemoService extends JeecgService<JeecgDemo> {

    public void testTran();

    public JeecgDemo getByIdCacheable(String id);

    /**
     * 查询列表数据 在service中获取数据权限sql信息
     *
     * @param pageSize
     * @param pageNo
     * @return
     */
    IPage<JeecgDemo> queryListWithPermission(int pageSize, int pageNo);
}
