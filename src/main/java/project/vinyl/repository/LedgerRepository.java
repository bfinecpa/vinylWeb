package project.vinyl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.vinyl.dto.LedgerFormDto;
import project.vinyl.entity.Ledger;

import java.util.List;

public interface LedgerRepository extends JpaRepository<Ledger, Long> {

    List<Ledger> findByMemberId(Long memberId);

    @Query("select new project.vinyl.dto.LedgerFormDto(l.id, l.name, l.detail, l.buyOrSell, l.price) " +
            "from Ledger l " +
            "where l.member.id = :memberId")
    List<LedgerFormDto> getLedgerDto(@Param("memberId") Long memberId);
}
