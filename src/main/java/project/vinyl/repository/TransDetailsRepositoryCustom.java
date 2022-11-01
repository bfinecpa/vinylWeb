package project.vinyl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.vinyl.dto.TransactionDetailsDto;

public interface TransDetailsRepositoryCustom {
    Page<TransactionDetailsDto> getTransaction(Long memberId, Pageable pageable);
}
