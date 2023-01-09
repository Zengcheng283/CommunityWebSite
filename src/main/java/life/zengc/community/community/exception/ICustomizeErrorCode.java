package life.zengc.community.community.exception;

/**
 * 让其他自定义Error实现该接口，减少代码复杂性
 */
public interface ICustomizeErrorCode {
    public String getMessage();
}
