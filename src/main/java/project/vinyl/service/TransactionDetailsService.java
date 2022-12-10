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
    private final MemberService memberService;
    private final MessageRoomService messageRoomService;

    private final ItemImgService itemImgService;
    private final LedgerService ledgerService;
    private final TotalLedgerService totalLedgerService;


    //거래 완료 기능
    public void saveTransactionCompleted(Long memberId, Long messageRoomId, Double tradingRate){
        Member member = memberService.findById(memberId);
        MessageRoom messageRoom = messageRoomService.findMessageRoomById(messageRoomId);

        Member participateMember1 = messageRoom.getParticipateMember1();
        Member participateMember2 = messageRoom.getParticipateMember2();

        if (participateMember1.getId()==memberId){
            participateMember2.updateRating(tradingRate);
        }
        if (participateMember2.getId()==memberId){
            participateMember1.updateRating(tradingRate);
        }

        List<ItemImg> byItemIdOrderByIdAsc = itemImgService.findItemImgsByitemId(messageRoom.getItem().getId());

        transDetailsRepository.save(TransactionDetails.builder()
                .itemName(messageRoom.getItem().getName())
                .itemId(messageRoom.getItem().getId())
                .price(messageRoom.getItem().getPrice())
                .itemImgUrl(byItemIdOrderByIdAsc.get(0).getImgUrl())
                .itemName(member.getName())
                .sellerName(member.getName())
                .member(member)
                .build());

        if(messageRoom.getItem().getMember().getId()==memberId){
            messageRoom.getItem().setItemSellStatus(ItemSellStatus.SOLD_OUT);
        }
        if(messageRoom.getItem().getMember().getId()==memberId){
            ledgerService.save(Ledger.builder()
                    .name(messageRoom.getItem().getName())
                    .member(member)
                    .detail("판매")
                    .buyOrSell(BuySell.SELL)
                    .price(messageRoom.getItem().getPrice())
                    .build());
            totalLedgerService.addPriceToTotalLedger(memberId,BuySell.SELL,messageRoom.getItem().getPrice());
        }
        if(messageRoom.getItem().getMember().getId()!=memberId){
            ledgerService.save(Ledger.builder()
                    .name(messageRoom.getItem().getName())
                    .member(member)
                    .detail("구매")
                    .buyOrSell(BuySell.BUY)
                    .price(messageRoom.getItem().getPrice())
                    .build());
            totalLedgerService.addPriceToTotalLedger(memberId,BuySell.BUY,messageRoom.getItem().getPrice());
        }
    }

    //거래 조회 기능
    public List<TransactionDetailsDto>  getTransDetailDto(Long memberId){
        return transDetailsRepository.getTransaction(memberId);
    }
}