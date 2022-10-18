package project.vinyl.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.vinyl.constant.ItemSellStatus;
import project.vinyl.dto.CRUDItemDto;
import project.vinyl.dto.ItemFormDto;
import project.vinyl.entity.Item;
import project.vinyl.entity.ItemImg;
import project.vinyl.entity.Member;
import project.vinyl.repository.ItemImgRepository;
import project.vinyl.repository.ItemRepository;
import project.vinyl.repository.UserRepository;

import javax.persistence.EntityExistsException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    @Autowired
    UserRepository userRepository;

    List<MultipartFile> createFile(){

        List<MultipartFile> multipartFiles = new ArrayList<>();

        for(int i =0; i<5; i++){
            String path = "C:/vinyl/item/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile mockMultipartFile =
                    new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
            multipartFiles.add(mockMultipartFile);
        }

        return multipartFiles;
    }

    List<MultipartFile> createUpdateFile(){

        List<MultipartFile> multipartFiles = new ArrayList<>();

        for(int i =0; i<5; i++){
            String path = "C:/vinyl/item/";
            String imageName = "updateImage" + i + ".jpg";
            MockMultipartFile mockMultipartFile =
                    new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
            multipartFiles.add(mockMultipartFile);
        }

        return multipartFiles;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    void saveItem() throws IOException {
        Member user = new Member();
        userRepository.save(user);

        ItemFormDto itemFormDto = ItemFormDto.builder()
                .name("테스트 상품")
                .details("테스트 상품 디테일")
                .price(1000)
                .stockNumber(5)
                .negotiation(true)
                .itemSellStatus(ItemSellStatus.SELL)
                .build();

        List<MultipartFile> file = createFile();
        //test할때 결국에 service에서 나오는 리턴값으로 비교를 해야하므로 service의 함수는 리턴이 있어야 한다.

        Long itemId = itemService.saveItem(itemFormDto, file, user.getId());

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        Item savedItem=itemRepository.findById(itemId).orElseThrow(EntityExistsException::new);

        assertEquals(itemFormDto.getName(), savedItem.getName());
        assertEquals(itemFormDto.getItemSellStatus() , savedItem.getItemSellStatus());
        assertEquals(itemFormDto.getDetails() , savedItem.getDetails());
        assertEquals(itemFormDto.getPrice() , savedItem.getPrice());
        assertEquals(itemFormDto.getStockNumber() , savedItem.getStockNumber());
        assertEquals(file.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());
    }

    @Test
    @DisplayName("유저 판매 내용 조회")
    void getCrud() throws IOException {

        Member user = new Member();
        userRepository.save(user);

        for(int i=0; i<10; i++){
            ItemFormDto itemFormDto = ItemFormDto.builder()
                    .name("테스트 상품"+i)
                    .details("테스트 상품 디테일" + i)
                    .price(1000)
                    .stockNumber(5)
                    .negotiation(true)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .build();

            List<MultipartFile> file = createFile();
            //test할때 결국에 service에서 나오는 리턴값으로 비교를 해야하므로 service의 함수는 리턴이 있어야 한다.
            itemService.saveItem(itemFormDto, file, user.getId());
        }
        PageRequest pageRequest = PageRequest.of(0,3);

        Page<CRUDItemDto> crudItem = itemService.getCRUDItem(user.getId(), pageRequest);
        List<CRUDItemDto> content = crudItem.getContent();
        assertEquals(content.size(), 3);
        assertEquals(content.get(0).getName(), "테스트 상품0");
    }

    @Test
    @DisplayName("수정")
    void update() throws IOException {
        Member user = new Member();
        userRepository.save(user);
        ItemFormDto itemFormDto = ItemFormDto.builder()
                .name("테스트 상품" + 1)
                .details("테스트 상품 디테일" + 1)
                .price(1000)
                .stockNumber(5)
                .negotiation(true)
                .itemSellStatus(ItemSellStatus.SELL)
                .build();

        List<MultipartFile> file = createUpdateFile();
        //test할때 결국에 service에서 나오는 리턴값으로 비교를 해야하므로 service의 함수는 리턴이 있어야 한다.
        Long id = itemService.saveItem(itemFormDto, file, user.getId());

        ItemFormDto updateItemFormDto = ItemFormDto.builder()
                .id(id)
                .name("테스트 상품" + 2)
                .details("테스트 상품 디테일" + 2)
                .price(1000)
                .stockNumber(5)
                .negotiation(true)
                .itemSellStatus(ItemSellStatus.SELL)
                .build();

        itemService.updateItem(updateItemFormDto,file,user.getId());

        Optional<Item> item = itemRepository.findById(id);
        Item item1 = item.get();

        List<ItemImg> byItemIdOrderByIdAsc = itemImgRepository.findByItemIdOrderByIdAsc(id);
        String oriImgName = byItemIdOrderByIdAsc.get(0).getOriImgName();
        log.info(item1.getName()+" "+oriImgName);
    }

    @Test
    @DisplayName("삭제")
    public void delete() throws IOException {
        Member user = new Member();
        userRepository.save(user);
        Long lastId = null;
        for(int i=0; i<10; i++){
            ItemFormDto itemFormDto = ItemFormDto.builder()
                    .name("테스트 상품"+i)
                    .details("테스트 상품 디테일" + i)
                    .price(1000)
                    .stockNumber(5)
                    .negotiation(true)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .build();

            List<MultipartFile> file = createFile();
            //test할때 결국에 service에서 나오는 리턴값으로 비교를 해야하므로 service의 함수는 리턴이 있어야 한다.
            lastId = itemService.saveItem(itemFormDto, file, user.getId());
        }

        itemService.deleteItem(lastId, user.getId());

        Optional<Item> byId = itemRepository.findById(lastId);
        if(byId.isEmpty()){
            log.info("ok");
        }
    }

}