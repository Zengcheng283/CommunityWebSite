package life.zengc.community.community.model;

import lombok.Data;

@Data
public class Comment {

    private Integer id;

    private Integer parentId;

    private Integer type;

    private Integer commentator;

    private Integer gmtCreate;

    private Integer gmtModified;

    private Integer likeCount;
}
