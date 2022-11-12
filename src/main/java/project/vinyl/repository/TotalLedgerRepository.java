package project.vinyl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.vinyl.entity.TotalLedger;

import java.util.Optional;

@Repository
public interface TotalLedgerRepository extends JpaRepository<TotalLedger, Long> {
    Optional<TotalLedger> findByMemberId(Long memberId);
}
