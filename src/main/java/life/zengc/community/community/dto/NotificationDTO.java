package life.zengc.community.community.dto;

import life.zengc.community.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {

    private String id;

    private User notifier;

    private Long gmtCreate;

    private Integer status;

    private String outerId;

    private String outerTitle;

    private Integer type;
}
