package com.jaramgroupware.mms.utils.mail;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum MailTemplate {
    DEV_ALERT("mms-dev-alert", List.of("content","name")),
    WELCOME("mms-welcome", List.of("name"));
    private final String name;
    private final List<String> args;
}
