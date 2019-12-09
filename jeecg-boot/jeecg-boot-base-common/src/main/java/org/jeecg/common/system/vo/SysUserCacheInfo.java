package org.jeecg.common.system.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.util.DateUtils;

import java.util.List;

@Data
@Accessors(chain = true)
public class SysUserCacheInfo {

    private String sysUserCode;

    private String sysUserName;

    private String sysOrgCode;

    private List<String> sysMultiOrgCode;

    private boolean oneDepart;

    public String getSysDate() {
        return DateUtils.formatDate();
    }

    public String getSysTime() {
        return DateUtils.now();
    }
}
