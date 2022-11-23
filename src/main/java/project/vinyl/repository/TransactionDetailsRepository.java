package project.vinyl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.vinyl.dto.TransactionDetailsDto;
import project.vinyl.entity.TransactionDetails;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Long>  {

    List<TransactionDetails> findByMessageRoomId(Long messageRoomId);

    @Query(value = "select " +
    "new project.vinyl.dto.TransactionDetailsDto(trans1.id, trans1.itemName, trans1.price, trans1.itemImgUrl, trans1.sellerName) " +
    "from TransactionDetails trans1 , TransactionDetails trans2 " +
    "inner join Member  mem on mem.id = trans1.member.id " +
    "where trans1.member.id = :memberId " +
    "and trans1.member.id <> trans2.member.id")
    List<TransactionDetailsDto>  getTransaction(@Param("memberId") Long memberId);

    List<TransactionDetails> findByMessageRoomItemId(Long itemId);

}
