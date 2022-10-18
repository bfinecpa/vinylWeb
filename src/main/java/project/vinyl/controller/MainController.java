package project.vinyl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.vinyl.dto.ItemSearchDto;
import project.vinyl.dto.MainPageItemDto;
import project.vinyl.service.ItemService;

import javax.print.attribute.standard.PageRanges;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final ItemService itemService;

    @GetMapping(value={"/", "/{page}"})
    public String mainPage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<MainPageItemDto> items = itemService.getMainPageItems(itemSearchDto, pageable);
        if(items.getContent().isEmpty()){
            log.info("비어있음");
        }
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        return "main";
    }
}
