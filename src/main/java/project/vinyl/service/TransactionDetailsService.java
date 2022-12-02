package project.vinyl.service;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.vinyl.constant.BuySell;
import project.vinyl.constant.ItemSellStatus;
import project.vinyl.dto.TransactionDetailsDto;
import project.vinyl.entity.*;
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

    private final LedgerRepository ledgerRepository;


    //거래 완료 기능
    public void saveTransactionCompleted(Long memberId, Long messageRoomId, Double tradingRate){
        Member member = memberRepository.findById(memberId).orElseThrow(EntityExistsException::new);
        member.updateRating(tradingRate);
        MessageRoom messageRoom = messageRoomRepository.findById(messageRoomId).orElseThrow(EntityExistsException::new);
        List<ItemImg> byItemIdOrderByIdAsc = itemImgRepository.findByItemIdOrderByIdAsc(messageRoom.getItem().getId());
        TransactionDetails transactionDetails =
                TransactionDetails.builder().itemName(messageRoom.getItem().getName())
                        .itemId(messageRoom.getItem().getId())
                        .price(messageRoom.getItem().getPrice()).itemImgUrl(byItemIdOrderByIdAsc.get(0).getImgUrl())
                        .itemName(member.getName()).sellerName(member.getName())
                        .member(member).build();

        transDetailsRepository.save(transactionDetails);
        if(messageRoom.getItem().getMember().getId()==memberId){
            messageRoom.getItem().setItemSellStatus(ItemSellStatus.SOLD_OUT);
        }
        if(messageRoom.getItem().getMember().getId()==memberId){
            Ledger ledger = new Ledger(messageRoom.getItem().getName(), member, "판매", BuySell.SELL, messageRoom.getItem().getPrice());
            ledgerRepository.save(ledger);
        }else{
            Ledger ledger = new Ledger(messageRoom.getItem().getName(), member, "구매", BuySell.BUY, messageRoom.getItem().getPrice());
        }
    }


    //거래 조회 기능
    public List<TransactionDetailsDto>  getTransDetailDto(Long memberId){
        List<TransactionDetailsDto> transactionDetailsDtos = transDetailsRepository.getTransaction(memberId);
        return transactionDetailsDtos;
    }

}