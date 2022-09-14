package cn.youai.xiuzhen.core.sharding;

import cn.hutool.core.date.DateUtil;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.Date;
import java.util.Properties;

/**
 * 自定义基于时间的分片
 **/
@Slf4j
@NoArgsConstructor
public class DateStandardShardingAlgorithm implements StandardShardingAlgorithm<Date> {

    private static final String SHARDING_SUFFIX_PATTERN_KEY = "sharding-suffix-pattern";

    @Getter
    private Properties props;
    private String dateTimePattern;

    @Override
    public void init(Properties properties) {
        this.props = properties;
        this.dateTimePattern = getDateTimePattern(props);
    }

    private String getDateTimePattern(final Properties props) {
        Preconditions.checkArgument(props.containsKey(SHARDING_SUFFIX_PATTERN_KEY), "%s can not be null.", SHARDING_SUFFIX_PATTERN_KEY);
        return props.getProperty(SHARDING_SUFFIX_PATTERN_KEY);
    }

    @Override
    public String doSharding(final Collection<String> availableTargetNames, final PreciseShardingValue<Date> shardingValue) {
        Date value = shardingValue.getValue();
        String suffix = DateUtil.format(value, dateTimePattern);
        return availableTargetNames.stream().filter(t -> t.endsWith(suffix)).findFirst().orElse(null);
    }

    @Override
    public Collection<String> doSharding(final Collection<String> availableTargetNames, final RangeShardingValue<Date> shardingValue) {
        return null;
    }

}
