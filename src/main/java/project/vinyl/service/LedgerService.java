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
    private final TotalLedgerService totalLedgerService;
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

        totalLedgerService.addPriceToTotalLedger(memberId, ledger.getBuyOrSell(), ledger.getPrice());
    }

    public void save(Ledger ledger){
        ledgerRepository.save(ledger);
    }

    public void updateLedger(LedgerFormDto ledgerFormDto, Long memberId){
        Ledger ledger = ledgerRepository.findById(ledgerFormDto.getId()).orElseThrow(EntityExistsException::new);

        totalLedgerService.changeValueFromSell(ledgerFormDto,memberId,ledger.getBuyOrSell(), ledger.getPrice());
        totalLedgerService.changeValueFromBuy(ledgerFormDto,memberId,ledger.getBuyOrSell(), ledger.getPrice());

        ledger.updateLedger(ledgerFormDto.getName(), ledgerFormDto.getDetail(),
                ledgerFormDto.getBuyOrSell(), ledgerFormDto.getPrice());
    }

    public LedgerFormDto findLedger(Long ledgerId){
        Ledger ledger = ledgerRepository.findById(ledgerId).orElseThrow(EntityExistsException::new);
        LedgerFormDto ledgerFormDto = LedgerFormDto.of(ledger);
        return ledgerFormDto;
    }

    public TotalLedgerDto getLedgers(Long memberId){
        List<LedgerFormDto> ledgerFormDtoList = ledgerRepository.getLedgerDto(memberId);
        TotalLedger totalLedger = totalLedgerService.findTotalLedgerById(memberId);
        return TotalLedgerDto.builder()
                .ledgerFormDtoList(ledgerFormDtoList)
                .totalIncome(totalLedger.getIncome())
                .totalExpense(totalLedger.getExpense())
                .build();
    }

    public void removeLedger(Long memberId, Long legerId){
        Ledger ledger = ledgerRepository.findById(legerId).get();
        if(ledger.getMember().getId() == memberId){
            if(ledger.getBuyOrSell()== BuySell.BUY){
               totalLedgerService.subtractIncome(memberId, ledger.getPrice());
            }
            if(ledger.getBuyOrSell()== BuySell.SELL){
                totalLedgerService.subtractExpense(memberId, ledger.getPrice());
            }
            ledgerRepository.delete(ledger);
        }
    }
}
