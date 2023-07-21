package com.jaramgroupware.mms.utils.time;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RequiredArgsConstructor
@Component
public class TimeUtility {


    private static final Clock clock = Clock.system(ZoneId.of("Asia/Seoul"));

    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now(clock);
    }

    public static LocalDate nowDate() {
        return LocalDate.now(clock);
    }
}
