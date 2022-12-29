package life.zengc.community.community.controller;

import life.zengc.community.community.mapper.UserMapper;
import life.zengc.community.community.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户进入网站主页，如果当前用户页面保存session且数据库内有相同session
     * 直接进入登录状态，否则需要登录
     * @param request
     * @return
     */
    @GetMapping("/")
    public String index(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie: cookies) {
            if (cookie.getName().equals("token")) {
                String token = cookie.getValue();
                Person person = userMapper.findByToken(token);
                if (person != null) {
                    request.getSession().setAttribute("user", person);
                }
                break;
            }
        }
        return "index";
    }
}
