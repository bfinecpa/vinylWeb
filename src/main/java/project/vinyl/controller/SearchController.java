package project.vinyl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.vinyl.dto.SearchDto;
import project.vinyl.service.SearchService;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public String home(@RequestParam(required = false, value = "keyword") String keyword, Model model){
        if (keyword != null) {
            model.addAttribute("hasKeyword", true);
            model.addAttribute("keyword", keyword);
            List<SearchDto> resultList = searchService.getResult(keyword);
            if (resultList != null) {
                model.addAttribute("hasResult", true);
                model.addAttribute("resultList", resultList);
            } else {
                model.addAttribute("hasResult", false);
            }
        } else {
            model.addAttribute("hasKeyword", false);
        }
        return "search/searchMain";
    }
}
