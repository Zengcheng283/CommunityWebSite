package life.zengc.community.community.controller;

import life.zengc.community.community.dto.PageDTO;
import life.zengc.community.community.dto.QuestionDTO;
import life.zengc.community.community.mapper.QuestionMapper;
import life.zengc.community.community.mapper.UserMapper;
import life.zengc.community.community.model.Question;
import life.zengc.community.community.model.User;
import life.zengc.community.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户进入网站主页，如果当前用户页面保存session且数据库内有相同session
     * 直接进入登录状态，否则需要登录
     *
     * @param request
     * @return
     */
    @GetMapping("/")
    public String index(
            HttpServletRequest request,
            Model model,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "8") Integer size) {
//        Cookie[] cookies = request.getCookies();
//        log.info(Arrays.toString(cookies));
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("token")) {
//                    String token = cookie.getValue();
//                    User user = userMapper.findByToken(token);
//                    if (user != null) {
//                        request.getSession().setAttribute("user", user);
//                    }
//                    break;
//                }
//            }
//        }
        PageDTO pageDTO = questionService.list(page, size);
        model.addAttribute("pageDTO", pageDTO);
        return "index";
    }
}
