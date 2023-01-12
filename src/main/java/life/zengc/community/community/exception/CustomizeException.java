package life.zengc.community.community.exception;

import lombok.Data;

@Data
public class CustomizeException extends RuntimeException{
    private String message;

    private Integer code;



    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }

    public CustomizeException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
