package com.jaramgroupware.mms.schedulers;

import com.jaramgroupware.mms.service.MemberService;
import com.jaramgroupware.mms.service.RegisterCodeService;
import com.jaramgroupware.mms.utils.mail.EmailSender;
import com.jaramgroupware.mms.utils.time.TimeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
            var cnt = memberService.processWithdrawal(timeUtility.nowDate());
            log.info("[WithdrawalScheduler] End process withdrawal, delete count : {}", cnt.size());
            emailSender.sendEmailToDev("[Success] WithdrawalScheduler","End process withdrawal, delete count : "+cnt.size());
        } catch (Exception e) {
            log.error("[WithdrawalScheduler] Error : {}", e.getMessage());
            emailSender.sendEmailToDev("[Error] WithdrawalScheduler","Fail. Error : "+e.getMessage());
        }

    }
}
