package life.zengc.community.community.service;

import life.zengc.community.community.common.CommonMethods;
import life.zengc.community.community.dto.CommentDTO;
import life.zengc.community.community.dto.PageDTO;
import life.zengc.community.community.dto.QuestionDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionService {

    @Autowired
    private CommonMethods commonMethods;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CommentMapper commentMapper;

    public PageDTO list(Integer page, Integer size) {
        Integer totalCount = questionMapper.count();
//        if (totalCount == 0) {
//            return new PageDTO();
//        }
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PageDTO pageDTO = new PageDTO();
        pageDTO.setQuestionDTOList(questionDTOList);
        pageDTO.setPage(totalCount, page, size);

        if (page < 1) {
            page = 1;
        }
        if (page > pageDTO.getTotalPage() && pageDTO.getTotalPage() > 0) {
            page = pageDTO.getTotalPage();
        }
        // offset = size * (page - 1)
        Integer offset = size * (page - 1);

        List<Question> questionList = questionMapper.list(offset, size);


        for (Question question : questionList) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }


        return pageDTO;
    }

    public PageDTO list(Integer page, Integer size, String id) {

        Integer totalCount = questionMapper.countById(id);

        log.info("totalCount: {}", totalCount);

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PageDTO pageDTO = new PageDTO();
        pageDTO.setQuestionDTOList(questionDTOList);
        pageDTO.setPage(totalCount, page, size);

        if (page < 1) {
            page = 1;
        }
        if (page > pageDTO.getTotalPage() && pageDTO.getTotalPage() > 0) {
            page = pageDTO.getTotalPage();
        }
        // offset = size * (page - 1)
        Integer offset = size * (page - 1);

        List<Question> questionList = questionMapper.listById(offset, size, id);


        for (Question question : questionList) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }


        return pageDTO;

    }

    public QuestionDTO getById(String id) {
        Question question = questionMapper.getById(id);

        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUSTION_NOT_FOUND);
        }

        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);

        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);

        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            // 创建
            String id = commonMethods.randomId();
            while (questionMapper.selectById(id) != null) {
                id = commonMethods.randomId();
            }
            question.setId(id);
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.create(question);
        } else {
            // 更新
            question.setGmtModified(System.currentTimeMillis());
            int updateResult = questionMapper.update(question);
            log.info(String.valueOf(updateResult));
            if (updateResult != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUSTION_NOT_FOUND);
            }
        }
    }

    public void incView(String id) {
        questionMapper.updateViewCount(id);
        return;
    }

    public void deletePublish(String id) {
        int result = questionMapper.deleteById(id);
        log.info("Delete result: {}", result);
        return;
    }

    public List<CommentDTO> listByQuestionId(String id) {
        List<Comment> commentList = commentMapper.selectByPIdAndType(id, CommentTypeEnum.QUESTION.getType());

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
