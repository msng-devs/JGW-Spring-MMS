package com.jaramgroupware.mms.schedulers;

import com.jaramgroupware.mms.dto.member.MemberDeletedResponseDto;
import com.jaramgroupware.mms.service.MemberService;
import com.jaramgroupware.mms.utils.mail.MailStormClient;
import com.jaramgroupware.mms.utils.time.TimeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class WithdrawalScheduler {
    private final MemberService memberService;
    private final TimeUtility timeUtility;

    @Scheduled(cron = "0 0 0 * * *")
    public void processWithdrawal() {
        log.info("[WithdrawalScheduler] Start process withdrawal");
        try{
            var result = memberService.processWithdrawal(timeUtility.nowDate());
            log.info("[WithdrawalScheduler] End process withdrawal, target count : {}", result.size());
            memberService.sendDevAlert("[MMS Scheduled] WithdrawalScheduler - 완료",
                    "회원 탈퇴 스케줄을 완료했습니다. <br> ---- 처리결과 ---- <br>"+toPretty(result));
        } catch (Exception e) {
            log.error("[WithdrawalScheduler] Error : {}", e.getMessage());
            memberService.sendDevAlert("[MMS Scheduled] WithdrawalScheduler - 실패","회원 탈퇴 스케줄 수행중 오류가 발생했습니다. 오류메시지 : "+e.getMessage());
        }

    }

    private String toPretty(List<MemberDeletedResponseDto> data){
        var sb = new StringBuilder();
        data.forEach(d -> sb.append("ID : ").append(d.getUid()).append("Email : ").append(d.getEmail()).append("Delete Status: ").append(d.isDeleted()).append("<br>"));
        return sb.toString();
    }
}
