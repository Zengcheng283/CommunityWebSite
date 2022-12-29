package life.zengc.community.community.controller;

import com.alibaba.fastjson.JSON;
import life.zengc.community.community.dto.AccessTokenDTO;
import life.zengc.community.community.dto.GithubUser;
import life.zengc.community.community.mapper.UserMapper;
import life.zengc.community.community.model.Person;
import life.zengc.community.community.provider.GithubProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 用户登录控制器
 */
@Slf4j
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectURI;

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
                           HttpServletResponse response){
        log.info("code: " + code);
        log.info("state: " + state);
        AccessTokenDTO accessTokenDTO = setAccessTokenDTOData(code, clientId, clientSecret, redirectURI, state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        log.info("UserName: "+ user.getName());
        if (user != null) {
            Person person = setPerson(user);
            userMapper.insert(person);
            response.addCookie(new Cookie("token", person.getToken()));
            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }

    /**
     * 设置用户状态
     * @param githubUser
     * @return
     */
    public Person setPerson(GithubUser githubUser) {
        Person person = new Person();
        person.setName(githubUser.getName());
        person.setToken(UUID.randomUUID().toString());
        person.setAccountId(githubUser.getId().toString());
        person.setGmtCreate(System.currentTimeMillis());
        person.setGmtModified(person.getGmtCreate());
        return person;
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
