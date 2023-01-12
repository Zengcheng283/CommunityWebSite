package life.zengc.community.community.dto;

import life.zengc.community.community.exception.CustomizeErrorCode;
import life.zengc.community.community.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO {

    private Integer code;

    private String message;

    private static ResultDTO createDTO(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO error(CustomizeErrorCode errorCode) {
        return createDTO(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO error(CustomizeException ex) {
        return createDTO(ex.getCode(), ex.getMessage());
    }

    public static ResultDTO ok() {
        return createDTO(200, "success");
    }
}
