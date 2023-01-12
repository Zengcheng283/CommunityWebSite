package life.zengc.community.community.controller;

import com.alibaba.fastjson2.JSON;
import life.zengc.community.community.dto.CommentDTO;
import life.zengc.community.community.dto.QuestionDTO;
import life.zengc.community.community.dto.ResultDTO;
import life.zengc.community.community.exception.CustomizeErrorCode;
import life.zengc.community.community.model.User;
import life.zengc.community.community.service.QuestionService;
import life.zengc.community.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
public class DetailsController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @GetMapping("/details/{id}")
    public String details(@PathVariable(name = "id") String id,
                          Model model) {
        questionService.incView(id);
        QuestionDTO questionDTO = questionService.getById(id);

        List<CommentDTO> commentDTOList = questionService.listByQuestionId(id);

        model.addAttribute("questionDTO", questionDTO);
        model.addAttribute("commentDTOList", commentDTOList);
        return "details";
    }

    @PostMapping ("/details/{id}")
    public Object deletePublish(@PathVariable(name = "id") String id,
                                HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        userService.selectUser(user.getToken());
        if (userService.selectUser(user.getToken())) {
            questionService.deletePublish(id);
            log.info("Delete question {} ok", id);
            return ResultDTO.ok();
        } else {
            return ResultDTO.error(CustomizeErrorCode.USER_ERROR);
        }

    }
}
