package com.jaramgroupware.mms.utils.mail;

import com.jaramgroupware.mms.service.MemberService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailSender {

    @Value("${mms.dev.mail}")
    private String from;
    private final JavaMailSender emailSender;
    private final MemberService memberService;

    public void sendEmailToDev(String title,String text) {
        var targets = memberService.findEmailsByRole(5L);
        targets.forEach(target -> {
            var message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(target);
            message.setSubject(title);
            message.setText(text);
            emailSender.send(message);
        });
    }
}
