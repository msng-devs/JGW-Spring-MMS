package com.jaramgroupware.mms.schedulers;

import com.jaramgroupware.mms.dto.member.MemberDeletedResponseDto;
import com.jaramgroupware.mms.service.MemberService;
import com.jaramgroupware.mms.service.RegisterCodeService;
import com.jaramgroupware.mms.utils.mail.EmailSender;
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
    private final EmailSender emailSender;

    @Scheduled(cron = "0 0 0 * * *")
    public void processWithdrawal() {
        log.info("[WithdrawalScheduler] Start process withdrawal");
        try{
            var result = memberService.processWithdrawal(timeUtility.nowDate());
            log.info("[WithdrawalScheduler] End process withdrawal, delete count : {}", result.size());
            emailSender.sendEmailToDev("[Success] WithdrawalScheduler","End process withdrawal, delete count : "+toPretty(result));
        } catch (Exception e) {
            log.error("[WithdrawalScheduler] Error : {}", e.getMessage());
            emailSender.sendEmailToDev("[Error] WithdrawalScheduler","Fail. Error : "+e.getMessage());
        }

    }

    private String toPretty(List<MemberDeletedResponseDto> data){
        var sb = new StringBuilder();
        data.forEach(d -> sb.append("ID : ").append(d.getUid()).append("Email : ").append(d.getEmail()).append("Delete Status: ").append(d.isDeleted()).append("\n"));
        return sb.toString();
    }
}
