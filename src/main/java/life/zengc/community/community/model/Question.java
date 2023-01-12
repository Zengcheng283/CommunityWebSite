package life.zengc.community.community.model;

import lombok.Data;

/**
 * 数据库question表的映射
 */
@Data
public class Question {

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
}
