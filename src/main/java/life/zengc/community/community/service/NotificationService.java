package life.zengc.community.community.service;

import life.zengc.community.community.common.CommonMethods;
import life.zengc.community.community.dto.NotificationDTO;
import life.zengc.community.community.dto.PageDTO;
import life.zengc.community.community.enums.NotificationStatusEnum;
import life.zengc.community.community.enums.NotificationTypeEnum;
import life.zengc.community.community.exception.CustomizeErrorCode;
import life.zengc.community.community.exception.CustomizeException;
import life.zengc.community.community.mapper.CommentMapper;
import life.zengc.community.community.mapper.NotificationMapper;
import life.zengc.community.community.mapper.QuestionMapper;
import life.zengc.community.community.mapper.UserMapper;
import life.zengc.community.community.model.Comment;
import life.zengc.community.community.model.Notification;
import life.zengc.community.community.model.Question;
import life.zengc.community.community.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    private CommonMethods commonMethods;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    public boolean notificationCreate(NotificationTypeEnum notificationTypeEnum,
                                          NotificationStatusEnum notificationStatusEnum,
                                          Comment comment,
                                          Comment resultComment
    ) {
        try {
            String id = commonMethods.randomId();
            while (notificationMapper.selectById(id) != null) {
                id = commonMethods.randomId();
            }
            Notification notification = new Notification();
            notification.setId(id);
            notification.setNotifier(comment.getCommentator());
            notification.setReceiver(resultComment.getCommentator());
            notification.setOuterId(comment.getParentId());
            notification.setType(notificationTypeEnum.getType());
            notification.setGmtCreate(System.currentTimeMillis());
            notification.setStatus(notificationStatusEnum.getStatus());
            notificationMapper.insert(notification);
            return false;
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return true;
        }
    }

    public boolean notificationCreate(NotificationTypeEnum notificationTypeEnum,
                                          NotificationStatusEnum notificationStatusEnum,
                                          Comment comment,
                                          Question resultQuestion
    ) {
        try {
            String id = commonMethods.randomId();
            while (notificationMapper.selectById(id) != null) {
                id = commonMethods.randomId();
            }
            Notification notification = new Notification();
            notification.setId(id);
            notification.setNotifier(comment.getCommentator());
            notification.setReceiver(resultQuestion.getCreator());
            notification.setOuterId(comment.getParentId());
            notification.setType(notificationTypeEnum.getType());
            notification.setGmtCreate(System.currentTimeMillis());
            notification.setStatus(notificationStatusEnum.getStatus());
            notificationMapper.insert(notification);
            return false;
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return true;
        }
    }

    public PageDTO<NotificationDTO> list(Integer page, Integer size, String id) {

        Integer totalCount = notificationMapper.countById(id);

        PageDTO<NotificationDTO> pageDTO = new PageDTO<>();
        pageDTO.setPage(totalCount, page, size);

        if (page < 1) {
            page = 1;
        }
        if (page > pageDTO.getTotalPage() && pageDTO.getTotalPage() > 0) {
            page = pageDTO.getTotalPage();
        }

        Integer offset = size * (page - 1);

        List<Notification> notificationList = notificationMapper.listById(offset, size, id);

        List<NotificationDTO> notificationDTOList = notificationList.stream().map(notification -> {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setId(notification.getId());
            notificationDTO.setGmtCreate(notification.getGmtCreate());
            notificationDTO.setStatus(notification.getStatus());
            notificationDTO.setOuterId(notification.getOuterId());

            if (NotificationTypeEnum.REPLY_QUESTION.getType() == notification.getType()) {
                Question question = questionMapper.getById(notification.getOuterId());
                notificationDTO.setOuterTitle(question.getTitle());
            } else if (NotificationTypeEnum.REPLY_COMMENT.getType() == notification.getType()) {
                Comment comment = commentMapper.selectById(notification.getOuterId());
                notificationDTO.setOuterTitle(comment.getContent());
            } else {
                throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_TYPE_ERROR);
            }

            User notifier = userMapper.findById(notification.getNotifier());
            notificationDTO.setNotifier(notifier);
            notificationDTO.setType(notification.getType());
            return notificationDTO;
        }).collect(Collectors.toList());

        pageDTO.setDTOList(notificationDTOList);

        return pageDTO;
    }

    public Integer getNotificationCount(String id) {
        return notificationMapper.countUndoById(id);
    }

    public void changeStatus(String id) {
        Notification notification = notificationMapper.getByOuterId(id);
        notificationMapper.changeStatusById(notification.getId());
    }
}
