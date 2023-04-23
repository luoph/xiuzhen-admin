package org.jeecg.common.system.vo;

import org.jeecg.common.util.DateUtils;

import java.util.List;

/**
 * @Description: 用户缓存信息
 * @author: jeecg-boot
 */
public class SysUserCacheInfo {

    private String sysUserCode;

    private String sysUserName;

    private String sysOrgCode;

    private String channel;

    private String sdkChannel;

    private List<String> sysMultiOrgCode;

    private boolean oneDepart;

    public boolean isOneDepart() {
        return oneDepart;
    }

    public void setOneDepart(boolean oneDepart) {
        this.oneDepart = oneDepart;
    }

    public String getSysDate() {
        return DateUtils.formatDate();
    }

    public String getSysTime() {
        return DateUtils.now();
    }

    public String getSysUserCode() {
        return sysUserCode;
    }

    public void setSysUserCode(String sysUserCode) {
        this.sysUserCode = sysUserCode;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public String getSysOrgCode() {
        return sysOrgCode;
    }

    public void setSysOrgCode(String sysOrgCode) {
        this.sysOrgCode = sysOrgCode;
    }

    public List<String> getSysMultiOrgCode() {
        return sysMultiOrgCode;
    }

    public void setSysMultiOrgCode(List<String> sysMultiOrgCode) {
        this.sysMultiOrgCode = sysMultiOrgCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSdkChannel() {
        return sdkChannel;
    }

    public void setSdkChannel(String sdkChannel) {
        this.sdkChannel = sdkChannel;
    }
}
