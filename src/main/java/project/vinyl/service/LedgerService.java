package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.vinyl.constant.BuySell;
import project.vinyl.dto.LedgerFormDto;
import project.vinyl.dto.TotalLedgerDto;
import project.vinyl.entity.Ledger;
import project.vinyl.entity.TotalLedger;
import project.vinyl.repository.LedgerRepository;
import project.vinyl.repository.TotalLedgerRepository;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LedgerService {

    private final LedgerRepository ledgerRepository;
    private final TotalLedgerRepository totalLedgerRepository;
    private final MemberService memberService;
    


    public void saveLedger(LedgerFormDto ledgerFormDto, Long memberId) {
        Ledger ledger = Ledger.builder()
                .name(ledgerFormDto.getName())
                .member(memberService.findById(memberId))
                .detail(ledgerFormDto.getDetail())
                .buyOrSell(ledgerFormDto.getBuyOrSell())
                .price(ledgerFormDto.getPrice())
                .build();
        ledgerRepository.save(ledger);
        addPriceToTotalLedger(memberId, ledger);
    }

    private void addPriceToTotalLedger(Long memberId, Ledger ledger) {
        TotalLedger totalLedger = totalLedgerRepository.findByMemberId(memberId).orElseThrow(EntityExistsException::new);
        if(ledger.getBuyOrSell()== BuySell.BUY){
            totalLedger.setIncome(totalLedger.getIncome()+ ledger.getPrice());
        }
        if(ledger.getBuyOrSell()== BuySell.SELL){
            totalLedger.setExpense(totalLedger.getExpense()+ ledger.getPrice());
        }
    }

    public void updateLedger(LedgerFormDto ledgerFormDto, Long memberId){
        Ledger ledger = ledgerRepository.findById(ledgerFormDto.getId()).orElseThrow(EntityExistsException::new);
        TotalLedger totalLedger = totalLedgerRepository.findByMemberId(memberId).orElseThrow(EntityExistsException::new);

        changeTotalLedgerValueFromBuy(ledgerFormDto, ledger, totalLedger);
        changeTotalLedgerValueFromSell(ledgerFormDto, ledger, totalLedger);

        ledger.updateLedger(ledgerFormDto.getName(), ledgerFormDto.getDetail(),
                ledgerFormDto.getBuyOrSell(), ledgerFormDto.getPrice());
    }

    private static void changeTotalLedgerValueFromSell(LedgerFormDto ledgerFormDto, Ledger ledger, TotalLedger totalLedger) {
        if(ledger.getBuyOrSell()==BuySell.SELL){
            if(ledgerFormDto.getBuyOrSell()==BuySell.BUY){
                totalLedger.setExpense(totalLedger.getExpense()- ledger.getPrice());
                totalLedger.setIncome(totalLedger.getIncome()+ ledgerFormDto.getPrice());
            }
            if(ledgerFormDto.getBuyOrSell()==BuySell.SELL){
                totalLedger.setExpense(totalLedger.getExpense()- ledger.getPrice()+ ledgerFormDto.getPrice());
            }
        }
    }

    private static void changeTotalLedgerValueFromBuy(LedgerFormDto ledgerFormDto, Ledger ledger, TotalLedger totalLedger) {
        if(ledger.getBuyOrSell()==BuySell.BUY){
            if(ledgerFormDto.getBuyOrSell()==BuySell.SELL){
                totalLedger.setIncome(totalLedger.getIncome()- ledger.getPrice());
                totalLedger.setExpense(totalLedger.getExpense()+ ledgerFormDto.getPrice());
            }
            if(ledgerFormDto.getBuyOrSell()==BuySell.BUY){
                totalLedger.setIncome(totalLedger.getIncome()- ledger.getPrice()+ ledgerFormDto.getPrice());
            }
        }
    }

    public LedgerFormDto findLedger(Long ledgerId){
        Ledger ledger = ledgerRepository.findById(ledgerId).orElseThrow(EntityExistsException::new);
        LedgerFormDto ledgerFormDto = LedgerFormDto.of(ledger);
        return ledgerFormDto;
    }

    public TotalLedgerDto getLedgers(Long memberId){
        List<LedgerFormDto> ledgerFormDtoList = ledgerRepository.getLedgerDto(memberId);
        TotalLedger totalLedger = totalLedgerRepository.findByMemberId(memberId).orElseThrow(EntityExistsException::new);
        return TotalLedgerDto.builder()
                .ledgerFormDtoList(ledgerFormDtoList)
                .totalIncome(totalLedger.getIncome())
                .totalExpense(totalLedger.getExpense())
                .build();
    }

    public void removeLedger(Long memberId, Long legerId){
        Ledger ledger = ledgerRepository.findById(legerId).get();
        TotalLedger totalLedger = totalLedgerRepository.findByMemberId(memberId).orElseThrow(EntityExistsException::new);
        if(ledger.getMember().getId() == memberId){
            if(ledger.getBuyOrSell()== BuySell.BUY){
                totalLedger.setIncome(totalLedger.getIncome()-ledger.getPrice());
            }
            if(ledger.getBuyOrSell()== BuySell.SELL){
                totalLedger.setExpense(totalLedger.getExpense()-ledger.getPrice());
            }
            ledgerRepository.delete(ledger);
        }
    }
}
