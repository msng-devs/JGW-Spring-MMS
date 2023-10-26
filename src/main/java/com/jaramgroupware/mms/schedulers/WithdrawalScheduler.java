package com.jaramgroupware.mms.schedulers;

import com.jaramgroupware.mms.dto.member.MemberDeletedResponseDto;
import com.jaramgroupware.mms.service.MemberService;
import com.jaramgroupware.mms.utils.mail.MailStormClient;
import com.jaramgroupware.mms.utils.others.TableBuilder;
import com.jaramgroupware.mms.utils.time.TimeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class WithdrawalScheduler {
    private final MailStormClient mailStormClient;
    private final MemberService memberService;
    private final TimeUtility timeUtility;
    private final List<String> resultRows = List.of("UID","EMAIL","DELETE STATUS");

    @Value("${mms.scheduler.result.to}")
    private String resultTo;

    @Scheduled(cron = "0 0 0 * * *")
    public void processWithdrawal() {
        log.info("[WithdrawalScheduler] Start process withdrawal");
        try{
            var result = memberService.processWithdrawal(timeUtility.nowDate());
            log.info("[WithdrawalScheduler] End process withdrawal, target count : {}", result.size());
            mailStormClient.sendAlertEmail(resultTo, "[MMS Scheduled] WithdrawalScheduler - 완료", Map.of("name", "관리자", "context", "회원 탈퇴 스케줄을 완료했습니다. <br> ---- 처리결과 ---- <br> "+toTableData(result)));
        } catch (Exception e) {
            log.error("[WithdrawalScheduler] Error : {}", e.getMessage());
            mailStormClient.sendAlertEmail(resultTo, "[MMS Scheduled] WithdrawalScheduler - 실패", Map.of("name", "관리자", "context", "회원 탈퇴 스케줄 수행중 오류가 발생했습니다. 오류메시지 : "+e.getMessage()));
        }

    }

    private String toTableData(List<MemberDeletedResponseDto> data){

        StringBuilder htmlTable = new StringBuilder();
        htmlTable.append("<table border=\"1\"><tr><th>UID</th><th>Email</th><th>Deleted</th></tr>");

        for (MemberDeletedResponseDto dto : data) {
            htmlTable.append("<tr>");
            // 각 열에 대한 데이터 추가
            htmlTable.append("<td>").append(dto.getUid()).append("</td>");
            htmlTable.append("<td>").append(dto.getEmail()).append("</td>");
            htmlTable.append("<td>").append(dto.isDeleted()).append("</td>");
            htmlTable.append("</tr>");
        }

        // 테이블 닫기
        htmlTable.append("</table>");

        return htmlTable.toString();
    }
}
