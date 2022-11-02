package project.vinyl.service;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.vinyl.dto.TransactionDetailsDto;
import project.vinyl.entity.Member;
import project.vinyl.entity.MessageRoom;
import project.vinyl.entity.TransactionDetails;
import project.vinyl.repository.MemberRepository;
import project.vinyl.repository.MessageRoomRepository;
import project.vinyl.repository.TransactionDetailsRepository;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionDetailsService {

    private final TransactionDetailsRepository transDetailsRepository;
    private final MemberRepository memberRepository;
    private final MessageRoomRepository messageRoomRepository;


    //거래 완료 기능
    public void saveTransactionCompleted(Long memberId, Long messageRoomId){
        Member member = memberRepository.findById(memberId).orElseThrow(EntityExistsException::new);
        MessageRoom messageRoom = messageRoomRepository.findById(messageRoomId).orElseThrow(EntityExistsException::new);
        TransactionDetails transactionDetails = new TransactionDetails(messageRoom, member);
        transDetailsRepository.save(transactionDetails);
    }


    //거래 조회 기능
    public List<TransactionDetailsDto>  getTransDetailDto(Long memberId){
        List<TransactionDetailsDto> transaction = transDetailsRepository.getTransaction(memberId);
        return transaction;
    }


}
