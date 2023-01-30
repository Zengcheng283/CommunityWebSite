package life.zengc.community.community.controller;

import life.zengc.community.community.dto.CommentDTO;
import life.zengc.community.community.dto.QuestionDTO;
import life.zengc.community.community.enums.CommentTypeEnum;
import life.zengc.community.community.service.CommentService;
import life.zengc.community.community.service.QuestionService;
import life.zengc.community.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class DetailsController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @GetMapping("/details/{id}")
    public Object details(@PathVariable(name = "id") String id,
                          Model model) {
        questionService.incView(id);
        QuestionDTO questionDTO = questionService.getById(id);
        List<Object> relatedQuestion = questionService.selectRelated(questionDTO);
        List<CommentDTO> commentDTOList = commentService.listByQuestionId(id, CommentTypeEnum.QUESTION);
        Map<String, Object> data = new HashMap<>();
        data.put("questionDTO", questionDTO);
//        data.put("commentDTOList", commentDTOList);
//        data.put("relatedQuestion", relatedQuestion);
        return data;
    }

    @ResponseBody
    @GetMapping("/related/{id}")
    public Object related(@PathVariable(name = "id") String id,
                          Model model) {
        QuestionDTO questionDTO = questionService.getById(id);
        return questionService.selectRelated(questionDTO);
    }

    @ResponseBody
    @GetMapping("/publisher/{id}")
    public Object publisher(@PathVariable(name = "id") String id,
                          Model model) {
        String userId = questionService.getCreator(id);
        return userService.selectUserById(userId);
    }
//    @PostMapping ("/details/{id}")
//    public Object deletePublish(@PathVariable(name = "id") String id,
//                                HttpServletRequest request) {
//        User user = (User) request.getSession().getAttribute("user");
//        userService.selectUser(user.getToken());
//        if (userService.selectUser(user.getToken())) {
//            questionService.deletePublish(id);
//            log.info("Delete question {} ok", id);
//            return ResultDTO.ok();
//        } else {
//            return ResultDTO.error(CustomizeErrorCode.USER_ERROR);
//        }
//
//    }
}
