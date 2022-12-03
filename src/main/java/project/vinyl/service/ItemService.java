package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.vinyl.dto.*;
import project.vinyl.entity.*;
import project.vinyl.repository.ItemImgRepository;
import project.vinyl.repository.ItemRepository;
import project.vinyl.repository.MemberRepository;
import project.vinyl.repository.UserRepository;

import javax.persistence.EntityExistsException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final MemberService memberService;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgList, Long memberId) throws IOException {

        Member member = memberService.findById(memberId);
        Item item = Item.builder()
                .name(itemFormDto.getName())
                .details(itemFormDto.getDetails())
                .price(itemFormDto.getPrice())
                .itemSellStatus(itemFormDto.getItemSellStatus())
                .stockNumber(itemFormDto.getStockNumber())
                .negotiation(itemFormDto.isNegotiation())
                .member(member)
                .build();
        itemRepository.save(item);
        itemImgService.saveItemImg(itemImgList, item);
        return item.getId();
    }

    public ItemFormDto getItemForm(Long itemId){

        Item item = itemRepository.findById(itemId).orElseThrow(EntityExistsException::new);
        
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        
        for (ItemImg itemImg : itemImgService.getItemImgList(itemId)) {
            itemImgDtoList.add(ItemImgDto.of(itemImg));
        }
        return ItemFormDto.of(item, itemImgDtoList);
    }


    //역할: 유저가 자기가 올린 상품 조회 => 대표 이미지 + 이름 + 가격 + 네고 여부...
    public Page<CRUDItemDto> getCRUDItem(Long itemId, Pageable pageable){
        
        Page<Item> items = itemRepository.findByMemberIdOrderByIdAsc(itemId, pageable);
        
        Page<CRUDItemDto> crudItemDtos = items.map(m -> CRUDItemDto.of(m));
        
        for (CRUDItemDto crudItemDto : crudItemDtos) {
            crudItemDto.setImgUrl(bringFirstImage(itemId));
        }
        return crudItemDtos;
    }

    private String bringFirstImage(Long itemId) {
        return itemImgService.getItemImgList(itemId).get(0).getImgUrl();
    }

    public void updateItem(ItemFormDto itemFormDto, List<MultipartFile> multipartFiles, Long id) throws IOException {
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityExistsException::new);
        if(item.getMember().getId()==id){
            item.updateItem(itemFormDto);
            itemImgService.update(multipartFiles, item);
        }
    }
    
    /**
    ---수정사항---
    다른 회원이 자신의 아이템 말고 다른 아이템을 삭제할 수 있음
    우선 자신의 memberId와 지우려는 item memeber id 확인 후 지운다.
     **/
    //기능 : 삭제
    public void deleteItem(Long itemId, Long userId){
        Item item = itemRepository.findById(itemId).orElseThrow(EntityExistsException::new);
        if (item.getMember().getId()==userId){
            itemImgService.delete(itemId);
            itemRepository.deleteById(itemId);
        }
    }

    /**
     * 메인페이지에서 가져오는 것
     */
    public Page<MainPageItemDto> getMainPageItems(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.search(itemSearchDto, pageable);
    }

    /**
     * 구매를 위한 상세페이지
     */
    public ItemDetailToDealDto getItemDetail(Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow(EntityExistsException::new);

        List<String> itemImgUrlList = new ArrayList<>();

        for (ItemImg itemImg : itemImgService.getItemImgList(itemId)) {
            itemImgUrlList.add(itemImg.getImgUrl());
        }

        return ItemDetailToDealDto.of(item, bringSellerIdFromItem(item), getTradingScore(item), itemImgUrlList);
    }

    private static Long bringSellerIdFromItem(Item item) {
        return item.getMember().getId();
    }

    private static String getTradingScore(Item item) {
        Double tradingRate = item.getMember().getTradingRate();
        Long countRatingPerson = item.getMember().getCountRatingPerson();
        Double rate = tradingRate/countRatingPerson;
        return String.format("%.1f", rate);
    }

}












