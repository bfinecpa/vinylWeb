package project.vinyl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.vinyl.service.SearchService;


@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public String home(@RequestParam(required = false, value = "keyword") String keyword){
        searchService.gimbab(keyword);
        return "search/searchMain";
    }

}
