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

    public Item findItemByItemId(Long itemId){
        return itemRepository.findById(itemId).orElseThrow(EntityExistsException::new);
    }

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


    //??????: ????????? ????????? ?????? ?????? ?????? => ?????? ????????? + ?????? + ?????? + ?????? ??????...
    public Page<CRUDItemDto> getCRUDItem(Long memberId, Pageable pageable){
        
        Page<Item> items = itemRepository.findByMemberIdOrderByIdAsc(memberId, pageable);
        
        Page<CRUDItemDto> crudItemDtos = items.map(m -> CRUDItemDto.of(m));
        
        for (CRUDItemDto crudItemDto : crudItemDtos) {
            crudItemDto.setImgUrl(bringFirstImage(crudItemDto.getId()));
        }
        return crudItemDtos;
    }

    private String bringFirstImage(Long itemId) {
        List<ItemImg> itemImgList = itemImgService.getItemImgList(itemId);
        if(!itemImgList.isEmpty()){
            return itemImgList.get(0).getImgUrl();
        }
        return null;
    }

    public void updateItem(ItemFormDto itemFormDto, List<MultipartFile> multipartFiles, Long id) throws IOException {
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityExistsException::new);
        if(item.getMember().getId()==id){
            item.updateItem(itemFormDto);
            itemImgService.update(multipartFiles, item);
        }
    }
    
    /**
    ---????????????---
    ?????? ????????? ????????? ????????? ?????? ?????? ???????????? ????????? ??? ??????
    ?????? ????????? memberId??? ???????????? item memeber id ?????? ??? ?????????.
     **/
    //?????? : ??????
    public void deleteItem(Long itemId, Long userId){
        Item item = itemRepository.findById(itemId).orElseThrow(EntityExistsException::new);
        if (item.getMember().getId()==userId){
            itemImgService.delete(itemId);
            itemRepository.deleteById(itemId);
        }
    }

    /**
     * ????????????????????? ???????????? ???
     */
    public Page<MainPageItemDto> getMainPageItems(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.search(itemSearchDto, pageable);
    }

    /**
     * ????????? ?????? ???????????????
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












