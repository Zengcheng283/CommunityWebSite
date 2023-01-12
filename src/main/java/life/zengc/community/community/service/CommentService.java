package life.zengc.community.community.service;

import life.zengc.community.community.common.CommonMethods;
import life.zengc.community.community.enums.CommentTypeEnum;
import life.zengc.community.community.exception.CustomizeErrorCode;
import life.zengc.community.community.exception.CustomizeException;
import life.zengc.community.community.mapper.CommentMapper;
import life.zengc.community.community.mapper.QuestionMapper;
import life.zengc.community.community.model.Comment;
import life.zengc.community.community.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class CommentService {

    @Autowired
    private CommonMethods commonMethods;

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;

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
            Comment resultComment = commentMapper.selectByPId(comment.getParentId());
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
}
