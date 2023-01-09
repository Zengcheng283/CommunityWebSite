package life.zengc.community.community.controller;
import life.zengc.community.community.dto.QuestionDTO;
import life.zengc.community.community.mapper.QuestionMapper;
import life.zengc.community.community.model.Question;
import life.zengc.community.community.model.User;
import life.zengc.community.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    /**
     * 新建问题对象并返回
     *
     * @param title
     * @param description
     * @param creator
     * @param tag
     * @return
     */
    private Question setQuestion(String title,
                                 String description,
                                 Integer creator,
                                 String tag,
                                 Integer id) {
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setCreator(creator);
        question.setTag(tag);
        question.setId(id);
        return question;
    }

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @PostMapping("/publish")
    public String publish(@RequestParam("title") String title,
                          @RequestParam("description") String description,
                          @RequestParam("tag") String tag,
                          HttpServletRequest request,
                          Model model) {

        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

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


        Question question = setQuestion(
                title,
                description,
                user.getId(),
                tag,
                null);

        questionService.createOrUpdate(question);

        return "redirect:/";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model) {
        QuestionDTO questionDTO = questionService.getById(id);
        model.addAttribute("questionTitle", questionDTO.getTitle());
        model.addAttribute("questionDescription", questionDTO.getDescription());
        model.addAttribute("questionTag", questionDTO.getTag());
        model.addAttribute("id", questionDTO.getId());
        return "publish";
    }
}
