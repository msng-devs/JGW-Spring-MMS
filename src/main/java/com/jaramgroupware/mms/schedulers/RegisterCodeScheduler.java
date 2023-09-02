package com.jaramgroupware.mms.schedulers;

import com.jaramgroupware.mms.service.RegisterCodeService;
import com.jaramgroupware.mms.utils.mail.MailStormClient;
import com.jaramgroupware.mms.utils.time.TimeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Slf4j
@RequiredArgsConstructor
@Component
public class RegisterCodeScheduler {

    private final RegisterCodeService registerCodeService;
    private final TimeUtility timeUtility;
    private final MailStormClient mailStormClient;
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanExpiredCode() {
        log.info("[RegisterCodeScheduler] Start clean expired code");

        try {
            var cnt = registerCodeService.findAllExpiredAndDel(timeUtility.nowDate());
            log.info("[RegisterCodeScheduler] End clean expired code, delete count : {}", cnt);
            mailStormClient.sendEmailToDev("[MMS Scheduled] RegisterCodeScheduler - 완료.","만료된 코드 삭제에 성공했습니다. 삭제된 코드 개수 : "+cnt);
        } catch (Exception e) {
            log.error("[RegisterCodeScheduler] Error : {}", e.getMessage());
            mailStormClient.sendEmailToDev("[MMS Scheduled] RegisterCodeScheduler - 실패.","만료된 코드 삭제에 실패했습니다. 오류메시지: "+e.getMessage());
        }


    }
}
