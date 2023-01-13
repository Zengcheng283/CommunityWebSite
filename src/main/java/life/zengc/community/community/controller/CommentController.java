package life.zengc.community.community.controller;

import com.alibaba.fastjson2.JSON;
import life.zengc.community.community.dto.CommentDTO;
import life.zengc.community.community.dto.ResultDTO;
import life.zengc.community.community.enums.CommentTypeEnum;
import life.zengc.community.community.exception.CustomizeErrorCode;
import life.zengc.community.community.model.Comment;
import life.zengc.community.community.model.User;
import life.zengc.community.community.service.CommentService;
import life.zengc.community.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request) {
        Map<String, String> data = new HashMap<>();
        if (commentDTO == null || StringUtils.isBlank(commentDTO.getContent())) {
            data.put("running", "error");
            return ResultDTO.error(CustomizeErrorCode.NO_COMMENT, data);
        }

        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            data.put("running", "error");
            return ResultDTO.error(CustomizeErrorCode.NO_LOGIN, data);
        }

        Comment comment = new Comment();
        comment.setParentId(commentDTO.getId());
        comment.setType(commentDTO.getType());
        comment.setContent(commentDTO.getContent());
        comment.setCommentator(user.getId());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setLikeCount(0L);
        commentService.insert(comment);
        data.put("running", "success");
        return ResultDTO.ok(data);
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comment(@PathVariable(name = "id") String id) {
        List<CommentDTO> commentDTOList = commentService.listByQuestionId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.ok(commentDTOList);
    }
}
