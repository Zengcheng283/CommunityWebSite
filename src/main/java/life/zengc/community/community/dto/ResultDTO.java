package life.zengc.community.community.dto;

import life.zengc.community.community.exception.CustomizeErrorCode;
import life.zengc.community.community.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO<T> {

    private Integer code;

    private String message;

    // 范型T
    private T data;

    private static <T> ResultDTO createDTO(Integer code, String message, T data) {
        ResultDTO<T> resultDTO = new ResultDTO<>();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        resultDTO.setData(data);
        return resultDTO;
    }

    public static <T> ResultDTO error(CustomizeErrorCode errorCode, T data) {
        return createDTO(errorCode.getCode(), errorCode.getMessage(), data);
    }

    public static <T> ResultDTO error(CustomizeException ex, T data) {
        return createDTO(ex.getCode(), ex.getMessage(), data);
    }

    public static <T> ResultDTO ok(T data) {
        return createDTO(200, "success", data);
    }
}
