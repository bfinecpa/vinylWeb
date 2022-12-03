package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.vinyl.constant.BuySell;
import project.vinyl.dto.LedgerFormDto;
import project.vinyl.entity.Ledger;
import project.vinyl.entity.Member;
import project.vinyl.entity.TotalLedger;
import project.vinyl.repository.TotalLedgerRepository;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TotalLedgerService {

    private final TotalLedgerRepository totalLedgerRepository;

    public void makeNewTotalLedger(Member member){
        TotalLedger totalLedger = new TotalLedger(member);
        totalLedgerRepository.save(totalLedger);
    }

    public TotalLedger findTotalLedgerById(Long memberId){
        return totalLedgerRepository.findByMemberId(memberId).orElseThrow(EntityExistsException::new);
    }

    public void subtractIncome(Long memberId, int price){
        TotalLedger totalLedger = findTotalLedgerById(memberId);
        totalLedger.setIncome(totalLedger.getIncome()-price);
    }

    public void subtractExpense(Long memberId, int price){
        TotalLedger totalLedger = findTotalLedgerById(memberId);
        totalLedger.setExpense(totalLedger.getExpense()-price);
    }

    public void addPriceToTotalLedger(Long memberId, BuySell buyOrSell, int price) {
        TotalLedger totalLedger = totalLedgerRepository.findByMemberId(memberId).orElseThrow(EntityExistsException::new);
        if(buyOrSell== BuySell.BUY){
            totalLedger.setIncome(totalLedger.getIncome() + price);
        }
        if(buyOrSell== BuySell.SELL){
            totalLedger.setExpense(totalLedger.getExpense() + price);
        }
    }

    public void changeValueFromSell(LedgerFormDto ledgerFormDto, Long memberId, BuySell prevBuySell, int prevPrice) {
        TotalLedger totalLedger = totalLedgerRepository.findByMemberId(memberId).orElseThrow(EntityExistsException::new);
        if(prevBuySell==BuySell.SELL){
            if(ledgerFormDto.getBuyOrSell()==BuySell.BUY){
                totalLedger.setExpense(totalLedger.getExpense()- prevPrice);
                totalLedger.setIncome(totalLedger.getIncome()+ ledgerFormDto.getPrice());
            }
            if(ledgerFormDto.getBuyOrSell()==BuySell.SELL){
                totalLedger.setExpense(totalLedger.getExpense()- prevPrice+ ledgerFormDto.getPrice());
            }
        }
    }

    public void changeValueFromBuy(LedgerFormDto ledgerFormDto, Long memberId, BuySell prevBuySell, int prevPrice) {
        TotalLedger totalLedger = totalLedgerRepository.findByMemberId(memberId).orElseThrow(EntityExistsException::new);
        if(prevBuySell==BuySell.BUY){
            if(ledgerFormDto.getBuyOrSell()==BuySell.SELL){
                totalLedger.setIncome(totalLedger.getIncome()- prevPrice);
                totalLedger.setExpense(totalLedger.getExpense()+ ledgerFormDto.getPrice());
            }
            if(ledgerFormDto.getBuyOrSell()==BuySell.BUY){
                totalLedger.setIncome(totalLedger.getIncome()- prevPrice+ ledgerFormDto.getPrice());
            }
        }
    }

}
