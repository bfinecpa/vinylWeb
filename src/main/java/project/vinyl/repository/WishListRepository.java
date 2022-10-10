package project.vinyl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.vinyl.entity.WishList;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    WishList findByMemberId(Long memberId);
}
