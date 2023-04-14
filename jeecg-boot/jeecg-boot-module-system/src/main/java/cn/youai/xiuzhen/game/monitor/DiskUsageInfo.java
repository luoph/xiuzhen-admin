package cn.youai.xiuzhen.game.monitor;

import cn.youai.basics.utils.SplitUtils;
import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.utils.BigDecimalUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 磁盘使用信息
 *
 * @author luopeihuan
 * @version 1.0
 * @date 2023/04/14
 */
@Data
@Accessors(chain = true)
public class DiskUsageInfo {
    private String fileSystem;
    private BigDecimal usedPer;
    private String used;
    private String avail;
    private String diskSize;

    public static DiskUsageInfo valueOf(String diskUsage) {
        // /:14.4G/49.09G,/data:165.22G/295.17G
        String[] array = diskUsage.split(":");
        if (array.length < 4) {
            return null;
        }

        DiskUsageInfo entity = new DiskUsageInfo();
        BigDecimal totalSize = new BigDecimal(array[1].replace("G", ""));
        BigDecimal usedSize = new BigDecimal(array[2].replace("G", ""));
        entity.setUsedPer(BigDecimalUtils.divideZero(usedSize, totalSize, true));
        entity.setFileSystem(array[0]);
        entity.setDiskSize(array[1]);
        entity.setUsed(array[2]);
        entity.setAvail(array[3]);
        return entity;
    }

    public static List<DiskUsageInfo> parse(String diskUsage) {
        if (StringUtils.isEmpty(diskUsage)) {
            return new ArrayList<>();
        }
        List<DiskUsageInfo> list = new ArrayList<>();
        List<String> split = SplitUtils.split(diskUsage, ",");
        for (String s : split) {
            list.add(valueOf(s));
        }
        list.sort(Comparator.comparing(DiskUsageInfo::getFileSystem));
        return list;
    }

}
