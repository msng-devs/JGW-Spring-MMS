package com.jaramgroupware.mms.utils.mail;

import com.google.gson.Gson;
import com.jaramgroupware.mms.service.MemberService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

import java.util.Map;

import static com.jaramgroupware.mms.utils.mail.MailTemplate.DEV_ALERT;
import static com.jaramgroupware.mms.utils.mail.MailTemplate.WELCOME;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailStormClient {

    @Value("${mailstorm.host}")
    private String host;

    @Value("${mailstorm.port}")
    private String port;

    private final Gson gson = new Gson();
    private final MemberService memberService;

    public void sendAlertEmail(String to,String title, Map<String,String> args) {
        var mailReq = MailSendRequest.builder()
                .to(to)
                .subject(title)
                .template(DEV_ALERT.getName())
                .args(args)
                .who("MMS")
                .build();
        var mailString = gson.toJson(mailReq);
        push(mailString);
    }
    public void sendWelcomeEmail(String to,String title, Map<String,String> args) {
        var mailReq = MailSendRequest.builder()
                .to(to)
                .subject(title)
                .template(WELCOME.getName())
                .args(args)
                .who("MMS")
                .build();
        var mailString = gson.toJson(mailReq);
        push(mailString);
    }

    public void sendEmailToDev(String title, String content) {
        var targets = memberService.findEmailsByRole(5L);
        if(targets.size() > 0) targets.forEach(t -> sendAlertEmail(t.getEmail(),title,Map.of("content",content,"name",t.getName())));
    }

    private void push(String message) {
        log.info("MailStorm Client Push Message : {}", message);
        try (var zContext = ZMQ.context(1)) {
            var zSocket = zContext.socket(ZMQ.PUSH);
            zSocket.connect("tcp://" + host + ":" + port);
            zSocket.send(message);
            zSocket.close();
            zContext.term();
        } catch (Exception e) {
            log.error("push message error : {}", e.getMessage());
        } finally {
            log.debug("push message end");
        }
    }

}
