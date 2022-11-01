package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.vinyl.dto.TransactionDetailsDto;
import project.vinyl.entity.MessageRoom;
import project.vinyl.entity.TransactionDetails;
import project.vinyl.repository.MessageRoomRepository;
import project.vinyl.repository.TransactionDetailsRepository;

import javax.persistence.EntityExistsException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionDetailsService {

    private final TransactionDetailsRepository transDetailsRepository;
    private final MessageRoomService messageRoomService;
    private final MessageRoomRepository messageRoomRepository;


    //거래 완료 기능
    public void saveTransactionCompleted(Long memberId, Long messageRoomId){
        Optional<TransactionDetails> byMessageRoomId = transDetailsRepository.findByMessageRoomId(messageRoomId);
        if(byMessageRoomId.isPresent()){
            TransactionDetails transactionDetails = byMessageRoomId.get();
            transactionDetails.getTransactionCompleted().put(memberId, true);
        }else{
            MessageRoom messageRoom = messageRoomRepository.findById(messageRoomId).orElseThrow(EntityExistsException::new);
            TransactionDetails transactionDetails = new TransactionDetails(messageRoom);
            transDetailsRepository.save(transactionDetails);
            transactionDetails.getTransactionCompleted().put(memberId, true);
        }
    }


    //거래 조회 기능
    public Page<TransactionDetailsDto> getTransDetailDto(Long memberId, Pageable pageable){
        Page<TransactionDetailsDto> transaction = transDetailsRepository.getTransaction(memberId, pageable);
        return transaction;
    }


}
