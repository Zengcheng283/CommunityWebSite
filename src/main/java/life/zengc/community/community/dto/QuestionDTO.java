package life.zengc.community.community.dto;

import life.zengc.community.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {

    private String id;

    private String title;

    private String description;

    private Long gmtCreate;

    private Long gmtModified;

    private String creator;

    private Integer commentCount;

    private Integer viewCount;

    private Integer likeCount;

    private String tag;

    private String creatorAvatar;

    private String creatorName;
}
