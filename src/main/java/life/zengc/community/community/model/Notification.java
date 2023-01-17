package life.zengc.community.community.model;

import lombok.Data;

@Data
public class Notification {

    private String id;

    private String notifier;

    private String receiver;

    private String outerId;

    private Integer type;

    private Long gmtCreate;

    private Integer status;
}
