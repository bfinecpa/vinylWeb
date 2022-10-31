package project.vinyl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.vinyl.constant.Role;
import project.vinyl.constant.SessionConst;
import project.vinyl.dto.CRUDItemDto;
import project.vinyl.dto.ItemDetailToDealDto;
import project.vinyl.dto.ItemFormDto;
import project.vinyl.entity.Member;
import project.vinyl.repository.MemberRepository;
import project.vinyl.service.ItemService;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
@Slf4j
public class ItemController {


    private final ItemService itemService;
    private final MemberRepository memberRepository;


    @GetMapping("/new")
    public String regItem(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping("/new")
    public String regItem(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                          HttpServletRequest request){

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId()==null){
            model.addAttribute("errorMessage", "첫번째 이미지는 필수입니다.");
            return "item/itemForm";
        }
        try{
            HttpSession session = request.getSession(false);
            Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
            itemService.saveItem(itemFormDto, itemImgFileList, member.getId());
        } catch (IOException e) {
            model.addAttribute("errorMessage", "상품 등록중 에러가 발생했습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = {"/manage", "/manage/{page}"})
    public String manageItem(@PathVariable("page") Optional<Integer> page, Model model,
                             HttpServletRequest request){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        Page<CRUDItemDto> crudItemDtos = itemService.getCRUDItem(member.getId(), pageable);

        model.addAttribute("crudItemDtos", crudItemDtos);
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }

    @GetMapping("/update/{itemId}")
    public String ItemDtl(@PathVariable("itemId") Long itemId, Model model){
        try{
            ItemFormDto itemFormDto = itemService.getItemForm(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping("/update/{itemId}")
    public String updateItem(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> multipartFileList, Model model){

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if(multipartFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 이미지는 필수입니다.");
            return "item/itemForm";
        }
        try{
            itemService.updateItem(itemFormDto, multipartFileList, 1L);
        } catch (IOException e) {
            model.addAttribute("errorMessage", "수정중 에러 발생");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    @GetMapping("/{itemId}")
    public String getItemDetail(@PathVariable("itemId") Long itemId, Model model){
        ItemDetailToDealDto itemDetail = itemService.getItemDetail(itemId);
        log.info(itemDetail.getId().getClass().toString());
        model.addAttribute("itemDetail", itemDetail);
        return "item/itemDetail";
    }

}
