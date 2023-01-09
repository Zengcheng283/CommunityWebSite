package life.zengc.community.community.controller;

import com.alibaba.fastjson.JSON;
import life.zengc.community.community.dto.AccessTokenDTO;
import life.zengc.community.community.dto.GithubUser;
import life.zengc.community.community.model.User;
import life.zengc.community.community.provider.GithubProvider;
import life.zengc.community.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 用户登录控制器
 */
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

    @Autowired
    private UserService userService;

    /**
     * 设置用户状态
     * @param githubUser
     * @return
     */
    private User setPerson(GithubUser githubUser) {
        User user = new User();
        user.setName(githubUser.getName());
        user.setToken(UUID.randomUUID().toString());
        user.setAccountId(githubUser.getId().toString());
        user.setAvatarUrl(githubUser.getAvatar_url());
        return user;
    }

    /**
     * 设置用户登录
     * @param code
     * @param clientId
     * @param clientSecret
     * @param redirectURI
     * @param state
     * @return
     */
    private AccessTokenDTO setAccessTokenDTOData(
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

    /**
     * 对返回的用户token插入数据库进行持久化操作
     * @param code
     * @param state
     * @param request
     * @return
     */
    @GetMapping("/callback")
    public String callBack(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        log.info("code: " + code);
        log.info("state: " + state);
        AccessTokenDTO accessTokenDTO = setAccessTokenDTOData(code, clientId, clientSecret, redirectURI, state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        log.info("UserName: "+ githubUser.getName());
        if (githubUser != null) {
            User user = setPerson(githubUser);
            user = userService.createOrUpdate(user);
            response.addCookie(new Cookie("token", user.getToken()));
            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
