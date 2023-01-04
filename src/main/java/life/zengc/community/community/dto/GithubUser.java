package life.zengc.community.community.dto;

import lombok.Data;

/**
 * 用户登录状态
 */
@Data
public class GithubUser {

    private String name;

    private Long id;

    private String bio;

    private String avatar_url;
}
