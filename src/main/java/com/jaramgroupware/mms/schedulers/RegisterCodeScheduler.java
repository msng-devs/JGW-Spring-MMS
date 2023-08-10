package com.jaramgroupware.mms.schedulers;

import com.jaramgroupware.mms.domain.registerCode.RegisterCode;
import com.jaramgroupware.mms.service.RegisterCodeService;
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

    @Scheduled(cron = "0 0 * * * ?")
    public void cleanExpiredCode() {
        log.info("[RegisterCodeScheduler] Start clean expired code");
        var cnt = registerCodeService.findAllExpiredAndDel(timeUtility.nowDate());
        log.info("[RegisterCodeScheduler] End clean expired code, delete count : {}", cnt);
    }
}
