package project.vinyl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.vinyl.dto.CRUDWishItemDto;

public interface WishItemRepositoryCustom {

    Page<CRUDWishItemDto> getWishItemDto(Long wishListId, Pageable pageable);
}
