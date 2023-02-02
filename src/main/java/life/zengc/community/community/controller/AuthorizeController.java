package life.zengc.community.community.controller;

import com.alibaba.fastjson.JSON;
import life.zengc.community.community.dto.AccessTokenDTO;
import life.zengc.community.community.dto.GithubUser;
import life.zengc.community.community.exception.CustomizeErrorCode;
import life.zengc.community.community.exception.CustomizeException;
import life.zengc.community.community.model.User;
import life.zengc.community.community.provider.GithubProvider;
import life.zengc.community.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户登录控制器
 */
@Slf4j
@RestController
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
     *
     * @param githubUser
     * @return
     */
    private User setPerson(GithubUser githubUser) {
        User user = new User();
        user.setId("");
        user.setName(githubUser.getName());
        user.setToken(UUID.randomUUID().toString());
        user.setAccountId(githubUser.getId().toString());
        user.setAvatarUrl(githubUser.getAvatar_url());
        return user;
    }

    /**
     * 设置用户登录
     *
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
     *
     * @param code
     * @param state
     * @param request
     * @return
     */
    @GetMapping("/callback")
    @ResponseBody
    public Object callBack(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        log.info("code: " + code);
        log.info("state: " + state);
        AccessTokenDTO accessTokenDTO = setAccessTokenDTOData(code, clientId, clientSecret, redirectURI, state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        log.info("UserName: " + githubUser.getName());
        if (githubUser != null) {
            User user = setPerson(githubUser);
            user = userService.createOrUpdate(user);
            response.addCookie(new Cookie("token", user.getToken()));
            return user.getToken();
        } else {
            return null;
        }
    }

    @GetMapping("/userInfo/{id}")
    public Object userInfo(@PathVariable(name = "id") String id) {
        Map<String, String> data = userService.getUserInfo(id);
        return data;

    }

    @PostMapping("/login")
    public Object login(@RequestBody Map<String, String> data) {
        log.info(data.toString());
        try {
            String token = userService.getUser(data.get("username"), data.get("password"));

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("token", token);

            return response;
        } catch (CustomizeException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", ex.getCode());
            response.put("message", ex.getMessage());

            return response;
        }

    }

    @PostMapping("/registered")
    public Object registered(@RequestBody Map<String, String> data) {
        log.info(data.toString());
        Map<String, Object> response = new HashMap<>();
        try {
            String username = data.get("username");
            String password = data.get("password");
            String phone = data.get("phone");
            boolean token = userService.checkExist(username, phone);
            if (!token) {
                User user = new User();
                user.setName(username);
                user.setPassword(password);
                user.setAccountId(phone);
                if (userService.createUser(user)) {
                    response.put("code", 200);
                    response.put("response", "success");
                    return response;
                } else {
                    response.put("code", CustomizeErrorCode.SERVICE_ERROR.getCode());
                    response.put("response", CustomizeErrorCode.SERVICE_ERROR.getMessage());
                    return response;
                }

            }
        } catch (CustomizeException ex) {

            response.put("code", ex.getCode());
            response.put("message", ex.getMessage());

            return response;
        }

        response.put("code", CustomizeErrorCode.SERVICE_ERROR.getCode());
        response.put("response", CustomizeErrorCode.SERVICE_ERROR.getMessage());
        return response;
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
