package group.yrmjhtdjxh.punch.VO;

import group.yrmjhtdjxh.punch.domain.Announcement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author dengg
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AnnouncementVO extends Announcement implements Serializable {
    /**
     * 点击次数
     */
    private Integer clickTimes;
}