package life.zengc.community.community.service;

import life.zengc.community.community.common.CommonMethods;
import life.zengc.community.community.dto.CommentDTO;
import life.zengc.community.community.enums.CommentTypeEnum;
import life.zengc.community.community.exception.CustomizeErrorCode;
import life.zengc.community.community.exception.CustomizeException;
import life.zengc.community.community.mapper.CommentMapper;
import life.zengc.community.community.mapper.QuestionMapper;
import life.zengc.community.community.mapper.UserMapper;
import life.zengc.community.community.model.Comment;
import life.zengc.community.community.model.Question;
import life.zengc.community.community.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentService {

    @Autowired
    private CommonMethods commonMethods;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId().equals("")) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_NOT_FOUND);
        }

        if (Objects.equals(comment.getType(), CommentTypeEnum.COMMENT.getType())) {
            // 回复评论
            Comment resultComment = commentMapper.selectById(comment.getParentId());
            if (resultComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            if (insertTest(comment)) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_ADD_ERROR);
            }
        } else {
            Question resultQuestion = questionMapper.selectByPId(comment.getParentId());
            if (resultQuestion == null) {
                throw new CustomizeException(CustomizeErrorCode.QUSTION_NOT_FOUND);
            }
            // 回复问题
            if (insertTest(comment)) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_ADD_ERROR);
            }
            questionMapper.incCommentCount(resultQuestion);
        }
    }

    private boolean insertTest(Comment comment) {
        try {
            String idKey = commonMethods.randomId();
            while (commentMapper.selectById(idKey) != null) {
                idKey = commonMethods.randomId();
            }
            comment.setId(idKey);
            commentMapper.insert(comment);
            return false;
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return true;
        }

    }

    public List<CommentDTO> listByQuestionId(String id, CommentTypeEnum commentTypeEnum) {
        List<Comment> commentList = commentMapper.selectByPIdAndType(id, commentTypeEnum.getType());

        if (commentList.size() == 0) {
            return new ArrayList<>();
        }

        // 流式编程，遍历list中每一个对象，获取commentator，在User表中进行查询后，直接拷贝入commentDTO，进行收集变为List

        Set<String> commentatorSet = commentList.stream()
                .map(Comment::getCommentator)
                .collect(Collectors.toSet());

        Map<String, User> userMap = commentatorSet.stream()
                .map(commentator -> userMapper.findById(commentator))
                .collect(Collectors.toMap(User::getId, user -> user));

        return commentList.stream()
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    BeanUtils.copyProperties(comment, commentDTO);
                    commentDTO.setUser(userMap.get(comment.getCommentator()));
                    return commentDTO;
                }).collect(Collectors.toList());
    }
}
