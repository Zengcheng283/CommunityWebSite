package life.zengc.community.community.dto;

import lombok.Data;

/**
 * 用户登录鉴权
 */
@Data
public class AccessTokenDTO {

    private String client_id;

    private String client_secret;

    private String code;

    private String redirect_uri;

    private String state;
}
