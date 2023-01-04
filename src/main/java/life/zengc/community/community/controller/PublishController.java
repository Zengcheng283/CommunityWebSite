package life.zengc.community.community.controller;

import life.zengc.community.community.common.CommonMethods;
import life.zengc.community.community.mapper.QuestionMapper;
import life.zengc.community.community.mapper.UserMapper;
import life.zengc.community.community.model.Question;
import life.zengc.community.community.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class PublishController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommonMethods commonMethods;

    @Autowired
    private QuestionMapper questionMapper;

    /**
     * 新建问题对象并返回
     * @param title
     * @param description
     * @param gmtCreate
     * @param creator
     * @param tag
     * @return
     */
    private Question setQuestion(String title,
                                 String description,
                                 Long gmtCreate,
                                 Integer creator,
                                 String tag) {
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setGmtCreate(gmtCreate);
        question.setGmtModified(gmtCreate);
        question.setCreator(creator);
        question.setTag(tag);
        return question;
    }

    @GetMapping("/publish")
    public String publish(HttpServletRequest request) {
        Cookie cookie = commonMethods.getTokenCookie(request, "token");

        if (cookie == null) {
            return "publish";
        }else {
            String token = cookie.getValue();
            User user = userMapper.findByToken(token);
            if (user != null) {
                request.getSession().setAttribute("user", user);
            }
        }
        return "publish";
    }

    @PostMapping("/publish")
    public String publish(@RequestParam("title") String title,
                          @RequestParam("description") String description,
                          @RequestParam("tag") String tag,
                          HttpServletRequest request,
                          Model model) {
        Cookie cookie = commonMethods.getTokenCookie(request, "token");

        log.info("title: {}", title);
        log.info("description: {}", description);
        log.info("tag: {}", tag);
        boolean reback = false;
        if (title == null || title.equals("")) {
            model.addAttribute("title", "标题不能为空");
            reback = true;
        }
        if (description == null || description.equals("")) {
            model.addAttribute("description", "补充不能为空");
            reback = true;
        }
        if (tag == null || tag.equals("")) {
            model.addAttribute("tag", "标签不能为空");
            reback = true;
        }
        if (reback) {
            return "publish";
        }
        User user = null;
        if (cookie != null) {
            log.info("request: {}", cookie.getValue());
            String token = cookie.getValue();
            user = userMapper.findByToken(token);
            if (user != null) {
                request.getSession().setAttribute("user", user);
            }
            Question question = setQuestion(
                    title,
                    description,
                    System.currentTimeMillis(),
                    user.getId(),
                    tag);
            questionMapper.create(question);
        }else {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        return "redirect:/";
    }
}
