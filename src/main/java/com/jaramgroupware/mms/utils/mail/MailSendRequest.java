package com.jaramgroupware.mms.utils.mail;

import lombok.*;

import java.util.Map;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailSendRequest {
    private String to;
    private String subject;
    private String template;
    private Map<String,String> arg;
    private String who;
}
