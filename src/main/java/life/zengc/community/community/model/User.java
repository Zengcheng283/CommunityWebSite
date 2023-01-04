package life.zengc.community.community.model;

import lombok.Data;

/**
 * 用户数据模型
 */
@Data
public class User {

    private Integer id;

    private String name;

    private String accountId;

    private String token;

    private Long gmtCreate;

    private Long gmtModified;

    private String avatarUrl;
}
