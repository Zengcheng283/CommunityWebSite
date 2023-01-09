package life.zengc.community.community.controller;

import life.zengc.community.community.dto.QuestionDTO;
import life.zengc.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailsController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/details/{id}")
    public String details(@PathVariable(name = "id") Integer id,
                          Model model) {
        questionService.incView(id);
        QuestionDTO questionDTO = questionService.getById(id);
        model.addAttribute("questionDTO", questionDTO);
        return "details";
    }
}
