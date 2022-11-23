package project.vinyl.service;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.vinyl.constant.ItemSellStatus;
import project.vinyl.dto.TransactionDetailsDto;
import project.vinyl.entity.ItemImg;
import project.vinyl.entity.Member;
import project.vinyl.entity.MessageRoom;
import project.vinyl.entity.TransactionDetails;
import project.vinyl.repository.*;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionDetailsService {

    private final TransactionDetailsRepository transDetailsRepository;
    private final MemberRepository memberRepository;
    private final MessageRoomRepository messageRoomRepository;

    private final ItemImgRepository itemImgRepository;


    //거래 완료 기능
    public void saveTransactionCompleted(Long memberId, Long messageRoomId){
        Member member = memberRepository.findById(memberId).orElseThrow(EntityExistsException::new);
        MessageRoom messageRoom = messageRoomRepository.findById(messageRoomId).orElseThrow(EntityExistsException::new);
        List<ItemImg> byItemIdOrderByIdAsc = itemImgRepository.findByItemIdOrderByIdAsc(messageRoom.getItem().getId());
        TransactionDetails transactionDetails =
                TransactionDetails.builder().itemName(messageRoom.getItem().getName())
                        .price(messageRoom.getItem().getPrice()).itemImgUrl(byItemIdOrderByIdAsc.get(0).getImgUrl())
                        .itemName(member.getName()).sellerName(member.getName())
                        .messageRoom(messageRoom)
                        .member(member).build();

        transDetailsRepository.save(transactionDetails);
        if(messageRoom.getItem().getMember().getId()==memberId){
            messageRoom.getItem().setItemSellStatus(ItemSellStatus.SOLD_OUT);
        }
    }


    //거래 조회 기능
    public List<TransactionDetailsDto>  getTransDetailDto(Long memberId){
        List<TransactionDetailsDto> transactionDetailsDtos = transDetailsRepository.getTransaction(memberId);
        return transactionDetailsDtos;
    }

    public void setNull(Long itemId){
        List<TransactionDetails> byMessageRoomItemId = transDetailsRepository.findByMessageRoomItemId(itemId);
        for (TransactionDetails transactionDetails : byMessageRoomItemId) {
            transactionDetails.setMessageRoom(null);
        }
    }

}