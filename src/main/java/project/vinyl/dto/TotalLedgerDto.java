package project.vinyl.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TotalLedgerDto {

    List<LedgerFormDto> ledgerFormDtoList;

    @Builder
    public TotalLedgerDto(List<LedgerFormDto> ledgerFormDtoList, int totalIncome, int totalExpense) {
        this.ledgerFormDtoList = ledgerFormDtoList;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
    }

    int totalIncome;

    int totalExpense;
}
