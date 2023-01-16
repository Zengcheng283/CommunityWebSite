package life.zengc.community.community.controller;

import life.zengc.community.community.dto.QuestionDTO;
import life.zengc.community.community.model.User;
import life.zengc.community.community.service.QuestionService;
import life.zengc.community.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class PersonalPageController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/person")
    public String personalPage(Model model,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            model.addAttribute("message", "用户未登录");
            return "error";
        }

        if (!userService.checkPerson(user)) {
            request.getSession().removeAttribute("user");
            Cookie cookie = new Cookie("token", null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            model.addAttribute("message", "用户错误请重新登录");
            return "error";
        }

        List<QuestionDTO> questionDTOList = questionService.getByCreator(user);

        User userResult = userService.selectUser(user);

        model.addAttribute("questionDTOList", questionDTOList);
        model.addAttribute("user", userResult);
        return "personalPage";
    }
}
