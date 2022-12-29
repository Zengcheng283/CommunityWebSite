package life.zengc.community.community.controller;

import com.alibaba.fastjson.JSON;
import life.zengc.community.community.dto.AccessTokenDTO;
import life.zengc.community.community.dto.GithubUser;
import life.zengc.community.community.provider.GithubProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Slf4j
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectURI;

    @GetMapping("/callback")
    public String callBack(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state){
        log.info("code: " + code);
        log.info("state: " + state);
        AccessTokenDTO accessTokenDTO = setAccessTokenDTOData(code, clientId, clientSecret, redirectURI, state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        log.info("UserName: "+ user.getName());
        return "index";
    }

    public AccessTokenDTO setAccessTokenDTOData(
            String code,
            String clientId,
            String clientSecret,
            String redirectURI,
            String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectURI);
        accessTokenDTO.setState(state);
        log.info(JSON.toJSONString(accessTokenDTO));
        return accessTokenDTO;
    }
}
