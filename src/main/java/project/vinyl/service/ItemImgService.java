package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.vinyl.dto.ItemImgDto;
import project.vinyl.entity.Item;
import project.vinyl.entity.ItemImg;
import project.vinyl.repository.ItemImgRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {

    private final ItemImgRepository itemImgRepository;


    @Value("${itemImgLocation}")
    String uploadPath;


    public List<ItemImg> findItemImgsByitemId(Long itemId){
        return itemImgRepository.findByItemIdOrderByIdAsc(itemId);
    }



    public  List<ItemImg> getItemImgList(Long id){
        return itemImgRepository.findByItemIdOrderByIdAsc(id);
    }

    public void saveItemImg(List<MultipartFile> multipartFileList, Item item) throws IOException {
        int idx = 0;
        for (MultipartFile multipartFile : multipartFileList) {
            String originalFilename = multipartFile.getOriginalFilename();
            if(originalFilename.isEmpty())break;
            String saveFilename = getSaveFilename(originalFilename);
            if(idx==0){
                ItemImg itemImg = ItemImg.builder().repImgYn("Y")
                        .imgName(saveFilename)
                        .imgUrl("/images/item/"+saveFilename)
                        .item(item)
                        .oriImgName(originalFilename)
                        .build();

                itemImgRepository.save(itemImg);
            }else{

                ItemImg itemImg = ItemImg.builder().repImgYn("N")
                        .imgName(saveFilename)
                        .imgUrl("/images/item/"+saveFilename)
                        .item(item)
                        .oriImgName(originalFilename)
                        .build();
                itemImgRepository.save(itemImg);
            }
            multipartFile.transferTo(new File(uploadPath+saveFilename));
            idx++;
        }
    }

    private String getSaveFilename(String originalFilename) {
        int extIndex = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(extIndex);// .png
        String uuid = String.valueOf(UUID.randomUUID());
        String saveFilename = uuid + ext;
        return saveFilename;
    }


    public void update(List<MultipartFile> multipartFiles, Item item) throws IOException {
        if(!multipartFiles.get(0).isEmpty()){
            //기존의 있는거 삭제
            List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(item.getId());
            for (ItemImg itemImg : itemImgList) {
                itemImgRepository.delete(itemImg);
                File imgFile = new File(uploadPath+itemImg.getImgName());
                imgFile.delete();
            }

            //새로운거 저장
            saveItemImg(multipartFiles,item);
        }

    }

    public void delete(Long id){
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(id);
        for (ItemImg itemImg : itemImgList) {
            File imgFile = new File(uploadPath+itemImg.getOriImgName());
            imgFile.delete();
        }

    }
}
