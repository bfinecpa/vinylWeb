package project.vinyl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.vinyl.dto.ItemSearchDto;
import project.vinyl.dto.MainPageItemDto;

public interface ItemRepositoryCustom {
    Page<MainPageItemDto> search(ItemSearchDto itemSearchDto, Pageable pageable);
}
