package project.vinyl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.vinyl.entity.TransactionDetails;

import java.util.Optional;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Long> , TransDetailsRepositoryCustom {

    Optional<TransactionDetails> findByMessageRoomId(Long messageRoomId);

}
