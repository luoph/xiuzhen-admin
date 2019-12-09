package org.jeecg.modules.message.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 消息模板
 * @date 2019-04-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("sys_sms_template")
public class SysMessageTemplate extends JeecgEntity {
    /**
     * 模板CODE
     */
    @Excel(name = "模板CODE", width = 15)
    private java.lang.String templateCode;
    /**
     * 模板标题
     */
    @Excel(name = "模板标题", width = 30)
    private java.lang.String templateName;
    /**
     * 模板内容
     */
    @Excel(name = "模板内容", width = 50)
    private java.lang.String templateContent;
    /**
     * 模板测试json
     */
    @Excel(name = "模板测试json", width = 15)
    private java.lang.String templateTestJson;
    /**
     * 模板类型
     */
    @Excel(name = "模板类型", width = 15)
    private java.lang.String templateType;
}
