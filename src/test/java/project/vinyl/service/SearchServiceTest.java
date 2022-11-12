package project.vinyl.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Slf4j
public class SearchServiceTest {
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
    @DisplayName("상품 검색")
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

}
