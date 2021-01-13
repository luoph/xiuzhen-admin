package cn.youai.xiuzhen.entity.pojo;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 时间范围
 *
 * @author lockr
 */
@Getter
@ToString
public class DateRange implements Serializable {
    /**
     * 序列化id
     */
    private static final long serialVersionUID = -2861210462050549677L;

    public DateRange(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    /**
     * 最小值
     */
    private final Date start;

    /**
     * 最大值
     */
    private final Date end;

    public boolean isValid() {
        return null != start && null != end;
    }


}
