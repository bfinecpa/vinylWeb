package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.vinyl.constant.BuySell;
import project.vinyl.dto.LedgerFormDto;
import project.vinyl.dto.TotalLedgerDto;
import project.vinyl.entity.Ledger;
import project.vinyl.entity.Member;
import project.vinyl.entity.TotalLedger;
import project.vinyl.repository.LedgerRepository;
import project.vinyl.repository.MemberRepository;
import project.vinyl.repository.TotalLedgerRepository;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LedgerService {

    private final LedgerRepository ledgerRepository;
    private final MemberRepository memberRepository;
    private final TotalLedgerRepository totalLedgerRepository;


    public void saveLedger(LedgerFormDto ledgerFormDto, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(EntityExistsException::new);
        Ledger ledger = new Ledger(ledgerFormDto.getName(), member , ledgerFormDto.getDetail(),ledgerFormDto.getBuyOrSell(), ledgerFormDto.getPrice());
        TotalLedger totalLedger = totalLedgerRepository.findByMemberId(memberId).orElseThrow(EntityExistsException::new);
        if(ledger.getBuyOrSell()== BuySell.BUY){
            totalLedger.setIncome(totalLedger.getIncome()+ledger.getPrice());
        }else{
            totalLedger.setExpense(totalLedger.getExpense()+ledger.getPrice());
        }
        ledgerRepository.save(ledger);
    }

    public void updateLedger(LedgerFormDto ledgerFormDto, Long memberId){
        Ledger ledger = ledgerRepository.findById(ledgerFormDto.getId()).orElseThrow(EntityExistsException::new);
        TotalLedger totalLedger = totalLedgerRepository.findByMemberId(memberId).orElseThrow(EntityExistsException::new);
        if(ledger.getBuyOrSell()==BuySell.BUY){
            if(ledgerFormDto.getBuyOrSell()==BuySell.SELL){
                totalLedger.setIncome(totalLedger.getIncome()-ledger.getPrice());
                totalLedger.setExpense(totalLedger.getExpense()+ledgerFormDto.getPrice());
            }else{
                totalLedger.setIncome(totalLedger.getIncome()-ledger.getPrice()+ledgerFormDto.getPrice());
            }
        }else{
            if(ledgerFormDto.getBuyOrSell()==BuySell.BUY){
                totalLedger.setExpense(totalLedger.getExpense()-ledger.getPrice());
                totalLedger.setIncome(totalLedger.getIncome()+ledgerFormDto.getPrice());
            }else{
                totalLedger.setExpense(totalLedger.getExpense()-ledger.getPrice()+ledgerFormDto.getPrice());
            }
        }
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
        TotalLedger totalLedger = totalLedgerRepository.findByMemberId(memberId).orElseThrow(EntityExistsException::new);
        int expense = totalLedger.getExpense();
        int income = totalLedger.getIncome();
        TotalLedgerDto totalLedgerDto = new TotalLedgerDto(ledgerFormDtoList, income, expense);
        return totalLedgerDto;
    }

    public void removeLedger(Long memberId, Long legerId){
        Optional<Ledger> byId = ledgerRepository.findById(legerId);
        Ledger ledger = byId.get();
        TotalLedger totalLedger = totalLedgerRepository.findByMemberId(memberId).orElseThrow(EntityExistsException::new);
        if(ledger.getBuyOrSell()== BuySell.BUY){
            totalLedger.setIncome(totalLedger.getIncome()-ledger.getPrice());
        }else{
            totalLedger.setExpense(totalLedger.getExpense()-ledger.getPrice());
        }
        if(ledger.getMember().getId() == memberId){
            ledgerRepository.delete(ledger);
        }
    }

}
