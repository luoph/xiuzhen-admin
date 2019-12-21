package org.jeecg.modules.game.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2019-12-21.
 */
@Data
@Accessors(chain = true)
public class NoticeConfig {
    private Long id;
    private Date beginTime;
    private Date endTime;
    private String content;
    private String title;
    private Integer noticeType;
    private Integer intervalSeconds;
}
