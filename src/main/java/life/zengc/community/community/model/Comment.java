package life.zengc.community.community.model;

import lombok.Data;

@Data
public class Comment {

    private String id;

    private String parentId;

    private Integer type;

    private String commentator;

    private Long gmtCreate;

    private Long gmtModified;

    private Long likeCount;

    private String content;
}
