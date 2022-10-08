package com.jaramgroupware.mms.utils.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateTimeCompare {

    public boolean compare(LocalDateTime pastDateTime, LocalDateTime futureDateTime){
        return pastDateTime.isBefore(futureDateTime) || pastDateTime.equals(futureDateTime);
    }
}
