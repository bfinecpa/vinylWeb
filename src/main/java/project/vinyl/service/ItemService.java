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
    private final UserRepository userRepository;

    private final TransactionDetailsService transactionDetailsService;

    // 상품 저장 , 유저 아이디는 어떻게 할 것인가?
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgList, Long userId) throws IOException {

        Item item = itemFormDto.createItem();
        Member member = userRepository.findById(userId).orElseThrow(EntityExistsException::new);
        item.setMember(member);
        itemRepository.save(item);

        // itemImg를 직접 만든다 이유는 ItemImg에는 Item이 들어가는데 이를 여기서 안넣으면 다른데서 문제가 된다.
        // item을 넘겨 주면? -> 의존관계가 어떻게 되는가 문제가 되는가?

        itemImgService.saveItemImg(itemImgList, item);
        return item.getId();
    }

    //상품의 자세한 내용 조회 ==> 상세내용 + 이미지
    public ItemFormDto getItemForm(Long itemId){

        Item item = itemRepository.findById(itemId).orElseThrow(EntityExistsException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);

        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        List<ItemImg> itemImgList = itemImgService.getItemImgList(itemId);

        for (ItemImg itemImg : itemImgList) {
            itemImgDtoList.add(ItemImgDto.of(itemImg));
        }
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }


    //역할: 유저가 자기가 올린 상품 조회 => 대표 이미지 + 이름 + 가격 + 네고 여부...
    public Page<CRUDItemDto> getCRUDItem(Long id, Pageable pageable){


        Page<Item> items = itemRepository.findByMemberIdOrderByIdAsc(id, pageable);


        Page<CRUDItemDto> crudItemDtos = items.map(m -> CRUDItemDto.of(m));


        for (CRUDItemDto crudItemDto : crudItemDtos) {
            List<ItemImg> itemImgs =itemImgService.getItemImgList(crudItemDto.getId());

            crudItemDto.setImgUrl(itemImgs.get(0).getImgUrl());
        }

        return crudItemDtos;
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
            transactionDetailsService.setNull(itemId);
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
        ItemDetailToDealDto itemDetailToDealDto = ItemDetailToDealDto.of(item);
        itemDetailToDealDto.setSellerId(item.getMember().getId());
        List<String> itemImgUrlList = new ArrayList<>();

        List<ItemImg> itemImgList = itemImgService.getItemImgList(itemId);

        for (ItemImg itemImg : itemImgList) {
            itemImgUrlList.add(itemImg.getImgUrl());
        }
        itemDetailToDealDto.setImgUrlList(itemImgUrlList);
        return itemDetailToDealDto;
    }


}












