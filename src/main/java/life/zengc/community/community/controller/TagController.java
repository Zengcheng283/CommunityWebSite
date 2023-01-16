package life.zengc.community.community.controller;

import life.zengc.community.community.dto.CommentDTO;
import life.zengc.community.community.dto.ResultDTO;
import life.zengc.community.community.dto.TagDTO;
import life.zengc.community.community.exception.CustomizeErrorCode;
import life.zengc.community.community.model.Comment;
import life.zengc.community.community.model.User;
import life.zengc.community.community.service.TagDTOService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TagController {

    @Autowired
    private TagDTOService tagDTOService;

    @ResponseBody
    @RequestMapping(value = "/tag", method = RequestMethod.POST)
    public Object post(@RequestBody TagDTO tagDTO,
                       HttpServletRequest request) {


        Map<String, String> data = new HashMap<>();
        if (tagDTO == null ||
                StringUtils.isBlank(tagDTO.getAlTagName())) {
            data.put("running", "error");
            return ResultDTO.error(CustomizeErrorCode.NO_COMMENT, data);
        }

        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            data.put("running", "error");
            return ResultDTO.error(CustomizeErrorCode.NO_LOGIN, data);
        }

        tagDTOService.createOrUpdate(tagDTO.getAlTagName(), tagDTO.getDetailTagName());

        return ResultDTO.ok(data);
    }
}
