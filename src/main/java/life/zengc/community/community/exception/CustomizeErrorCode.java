package life.zengc.community.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUSTION_NOT_FOUND(2001, "你找的问题不在了，要不换一个试试？"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
    NO_LOGIN(2003, "未登录，请先登录"),
    SERVICE_ERROR(2004, "服务器冒烟了，请稍等"),
    TYPE_PARAM_NOT_FOUND(2005, "评论类型错误"),
    COMMENT_NOT_FOUND(2006, "你回复的评论不存在"),
    USER_ERROR(2007, "你未拥有该权限"),
    COMMENT_ADD_ERROR(2008, "评论回复不成功"),
    NO_COMMENT(2009, "回复未输入内容"),
    DATA_BASE_INSERT_ERROR(2010, "数据库插入失败"),
    ;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private String message;

    private Integer code;

    CustomizeErrorCode(Integer code, String message) {

        this.message = message;
        this.code = code;
    }
}
