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
    "new project.vinyl.dto.TransactionDetailsDto(trans1.id, item.name, item.price, iimg.imgUrl, mem.name) " +
    "from TransactionDetails trans1 , TransactionDetails trans2 " +
    "inner join MessageRoom mr on mr.id = trans1.messageRoom.id " +
    "inner join Item item on item.id = mr.item.id " +
    "inner join ItemImg iimg on iimg.item.id = item.id " +
    "inner join Member  mem on mem.id = trans1.member.id " +
    "where trans1.messageRoom.id = trans2.messageRoom.id " +
    "and trans1.member.id = :memberId " +
    "and trans1.member.id <> trans2.member.id")
    List<TransactionDetailsDto>  getTransaction(@Param("memberId") Long memberId);

}
