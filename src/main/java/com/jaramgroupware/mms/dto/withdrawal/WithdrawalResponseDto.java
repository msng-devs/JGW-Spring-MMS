package com.jaramgroupware.mms.dto.withdrawal;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.withdrawal.Withdrawal;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WithdrawalResponseDto {
    private LocalDate withdrawalDate;

    public WithdrawalResponseDto(Withdrawal withdrawal) {
        this.withdrawalDate = withdrawal.getWithdrawalDate();
    }
}
