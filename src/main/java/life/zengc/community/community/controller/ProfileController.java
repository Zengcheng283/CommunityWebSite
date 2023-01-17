package life.zengc.community.community.controller;

import life.zengc.community.community.dto.NotificationDTO;
import life.zengc.community.community.dto.PageDTO;
import life.zengc.community.community.dto.QuestionDTO;
import life.zengc.community.community.mapper.UserMapper;
import life.zengc.community.community.model.User;
import life.zengc.community.community.service.NotificationService;
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

    @Autowired
    private NotificationService notificationService;

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

        if (Objects.equals(action, "published")) {
            PageDTO<QuestionDTO> pageDTO = questionService.list(page, size, user.getId());
            log.info("page: {}, size: {}, userID: {}", page, size, user.getId());
            model.addAttribute("section", action);
            model.addAttribute("sectionName", "我的发布");
            model.addAttribute("pageDTO", pageDTO);
        } else if (Objects.equals(action, "reply")) {
            PageDTO<NotificationDTO> pageDTO = notificationService.list(page, size, user.getId());
            log.info("page: {}, size: {}, userID: {}", page, size, user.getId());
            model.addAttribute("section", action);
            model.addAttribute("sectionName", "最新回复");
            model.addAttribute("pageDTO", pageDTO);
        }


        return "profile";
    }

    @GetMapping("/notification/{id}")
    public String notify(@PathVariable(name = "id") String id) {
        notificationService.changeStatus(id);
        return "redirect:/details/" + id;
    }
}
