package project.vinyl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import project.vinyl.entity.WishItem;

import java.util.List;

public interface WishItemRepository extends JpaRepository<WishItem, Long>, WishItemRepositoryCustom {

    List<WishItem> findByWishListId(Long wishListId);

}
