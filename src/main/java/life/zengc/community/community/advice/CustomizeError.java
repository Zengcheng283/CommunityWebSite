package life.zengc.community.community.advice;

import com.alibaba.fastjson.JSON;
import life.zengc.community.community.dto.ResultDTO;
import life.zengc.community.community.exception.CustomizeErrorCode;
import life.zengc.community.community.exception.CustomizeException;
import lombok.val;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class CustomizeError {

    @ExceptionHandler(Exception.class)
    ModelAndView handleControllerException(HttpServletRequest request,
                                     Throwable ex,
                                     Model model,
                                     HttpServletResponse response) {
        String contentType = request.getContentType();

        if (Objects.equals(contentType, "application/json")) {
            ResultDTO resultDTO = null;
            Map<String, String> data = new HashMap<>();
            // 返回JSON
            if (ex instanceof CustomizeException) {
                data.put("running", "error");
                resultDTO =  ResultDTO.error((CustomizeException) ex, data);
            } else {
                data.put("running", "error");
                resultDTO = ResultDTO.error(CustomizeErrorCode.SERVICE_ERROR, data);
            }

            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException IOex) {
            }
            return null;

        } else {
            //返回页面
            if (ex instanceof CustomizeException) {
                model.addAttribute("message", ex.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SERVICE_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.valueOf(statusCode);
        }
    }
}
