package cn.youai.xiuzhen.core.sharding;

import cn.hutool.core.date.DateUtil;
import cn.youai.server.utils.DateUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定义基于时间的分片
 **/
@Slf4j
@NoArgsConstructor
public class DateStandardShardingAlgorithm implements StandardShardingAlgorithm<Date> {

    private static final String DATE_TIME_LOWER_KEY = "datetime-lower";

    private static final String DATE_TIME_UPPER_KEY = "datetime-upper";

    private static final String SHARDING_SUFFIX_PATTERN_KEY = "sharding-suffix-pattern";

    private static final String INTERVAL_AMOUNT_KEY = "datetime-interval-amount";

    private static final String INTERVAL_UNIT_KEY = "datetime-interval-unit";

    @Getter
    private Properties props;
    private String dateTimePattern;

    private LocalDateTime dateTimeLower;

    private LocalDateTime dateTimeUpper;

    private DateTimeFormatter dateTimeFormatter;

    private int stepAmount;

    private ChronoUnit stepUnit;

    @Override
    public void init(Properties properties) {
        this.props = properties;
        this.dateTimePattern = getDateTimePattern(props);
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
        this.dateTimeLower = DateUtils.toLocalDateTime(getDateTimeLower(props));
        this.dateTimeUpper = DateUtils.toLocalDateTime(getDateTimeUpper(props));

        this.stepAmount = Integer.parseInt(props.getOrDefault(INTERVAL_AMOUNT_KEY, 1).toString());
        this.stepUnit = props.containsKey(INTERVAL_UNIT_KEY) ? getStepUnit(props.getProperty(INTERVAL_UNIT_KEY)) : ChronoUnit.DAYS;
    }

    private String getDateTimePattern(final Properties props) {
        Preconditions.checkArgument(props.containsKey(SHARDING_SUFFIX_PATTERN_KEY), "%s can not be null.", SHARDING_SUFFIX_PATTERN_KEY);
        return props.getProperty(SHARDING_SUFFIX_PATTERN_KEY);
    }

    private Date getDateTimeLower(final Properties props) {
        Preconditions.checkArgument(props.containsKey(DATE_TIME_LOWER_KEY), "%s can not be null.", DATE_TIME_LOWER_KEY);
        return getDateTime(props.getProperty(DATE_TIME_LOWER_KEY));
    }

    private Date getDateTimeUpper(final Properties props) {
        return props.containsKey(DATE_TIME_UPPER_KEY) ? getDateTime(props.getProperty(DATE_TIME_UPPER_KEY)) : DateUtils.now();
    }

    private Date getDateTime(final String dateTimeValue) {
        return DateUtils.parseDate(dateTimeValue);
    }

    private ChronoUnit getStepUnit(final String stepUnit) {
        for (ChronoUnit each : ChronoUnit.values()) {
            if (each.toString().equalsIgnoreCase(stepUnit)) {
                return each;
            }
        }
        throw new UnsupportedOperationException(String.format("Cannot find step unit for specified %s property: `%s`", INTERVAL_UNIT_KEY, stepUnit));
    }

    @Override
    public String doSharding(final Collection<String> availableTargetNames, final PreciseShardingValue<Date> shardingValue) {
        Date value = shardingValue.getValue();
        String suffix = DateUtil.format(value, dateTimePattern);
        return availableTargetNames.stream().filter(t -> t.endsWith(suffix)).findFirst().orElse(null);
    }

    @Override
    public Collection<String> doSharding(final Collection<String> availableTargetNames, final RangeShardingValue<Date> shardingValue) {
        return doSharding(availableTargetNames, shardingValue.getValueRange());
    }

    private Collection<String> doSharding(final Collection<String> availableTargetNames, final Range<Date> range) {
        return doShardingInLocalDateTime(availableTargetNames, range, dateTimeLower);
    }

    private Collection<String> doShardingInLocalDateTime(final Collection<String> availableTargetNames, final Range<Date> range, final LocalDateTime calculateTime) {
        Set<String> result = new HashSet<>();
        LocalDateTime calculateTimeAsView = calculateTime;
        LocalDateTime lower = range.hasLowerBound() ? parseLocalDateTime(range.lowerEndpoint()) : this.dateTimeUpper;
        LocalDateTime upper = range.hasUpperBound() ? parseLocalDateTime(range.upperEndpoint()) : this.dateTimeLower;
        while (!calculateTimeAsView.isAfter(upper)) {
            Range<LocalDateTime> timeRange = Range.closedOpen(calculateTimeAsView, calculateTimeAsView.plus(stepAmount, stepUnit));
            if (hasIntersection(timeRange, range, lower, upper)) {
                result.addAll(getMatchedTables(calculateTimeAsView, availableTargetNames));
            }
            calculateTimeAsView = calculateTimeAsView.plus(stepAmount, stepUnit);
        }
        return result;
    }

    private Collection<String> getMatchedTables(final TemporalAccessor dateTime, final Collection<String> availableTargetNames) {
        String tableSuffix = LocalDateTime.from(dateTime).format(dateTimeFormatter);
        return availableTargetNames.parallelStream().filter(each -> each.endsWith(tableSuffix)).collect(Collectors.toSet());
    }

    private boolean hasIntersection(final Range<LocalDateTime> calculateRange, final Range<Date> range, final LocalDateTime lower, final LocalDateTime upper) {
        BoundType lowerBoundType = range.hasLowerBound() ? range.lowerBoundType() : BoundType.CLOSED;
        BoundType upperBoundType = range.hasUpperBound() ? range.upperBoundType() : BoundType.CLOSED;
        Range<LocalDateTime> dateTimeRange = Range.range(lower, lowerBoundType, upper, upperBoundType);
        return calculateRange.isConnected(dateTimeRange) && !calculateRange.intersection(dateTimeRange).isEmpty();
    }

    private LocalDateTime parseLocalDateTime(Date endpoint) {
        if (endpoint instanceof java.sql.Date) {
            LocalDate localDate = ((java.sql.Date) endpoint).toLocalDate();
            return localDate.atStartOfDay();
        }

        return DateUtils.toLocalDateTime(endpoint);
    }

}
