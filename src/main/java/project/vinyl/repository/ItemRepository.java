package project.vinyl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.vinyl.entity.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> , ItemRepositoryCustom{

    Page<Item> findByMemberIdOrderByIdAsc(Long memberId, Pageable pageable);

    void deleteById(Long itemId);


}
