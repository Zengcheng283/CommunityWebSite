package life.zengc.community.community.dto;

import life.zengc.community.community.model.User;
import lombok.Data;

@Data
public class CommentDTO {

    private String id;

    private String parentId;

    private Integer type;

    private String commentator;

    private Long gmtCreate;

    private Long gmtModified;

    private Long likeCount;

    private String content;

    private User user;
}
