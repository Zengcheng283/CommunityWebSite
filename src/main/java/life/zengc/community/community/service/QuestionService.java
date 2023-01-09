package life.zengc.community.community.service;

import life.zengc.community.community.dto.PageDTO;
import life.zengc.community.community.dto.QuestionDTO;
import life.zengc.community.community.exception.CustomizeErrorCode;
import life.zengc.community.community.exception.CustomizeException;
import life.zengc.community.community.mapper.QuestionMapper;
import life.zengc.community.community.mapper.UserMapper;
import life.zengc.community.community.model.Question;
import life.zengc.community.community.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public PageDTO list(Integer page, Integer size) {
        Integer totalCount = questionMapper.count();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PageDTO pageDTO = new PageDTO();
        pageDTO.setQuestionDTOList(questionDTOList);
        pageDTO.setPage(totalCount, page, size);

        if (page < 1) {
            page = 1;
        }
        if (page > pageDTO.getTotalPage()) {
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

    public PageDTO list(Integer page, Integer size, Integer id) {

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

    public QuestionDTO getById(Integer id) {
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
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
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

    public void incView(Integer id) {
        questionMapper.updateViewCount(id);
        return;
    }
}
