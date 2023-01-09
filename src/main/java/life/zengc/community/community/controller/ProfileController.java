package life.zengc.community.community.controller;

import life.zengc.community.community.dto.PageDTO;
import life.zengc.community.community.mapper.UserMapper;
import life.zengc.community.community.model.User;
import life.zengc.community.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("replaceNumber", "14");

        if (Objects.equals(action, "published")) {
            model.addAttribute("section", "published");
            model.addAttribute("sectionName", "我的发布");
        } else if (Objects.equals(action, "replace")) {
            model.addAttribute("section", "replace");
            model.addAttribute("sectionName", "最新回复");
        }


        log.info("page: {}, size: {}, userID: {}", page, size, user.getId());
        PageDTO pageDTO = questionService.list(page, size, user.getId());


        model.addAttribute("pageDTO", pageDTO);

        return "profile";
    }
}
