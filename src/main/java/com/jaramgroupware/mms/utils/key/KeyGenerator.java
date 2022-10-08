package com.jaramgroupware.mms.utils.key;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class KeyGenerator {

    private final Random random = new Random();

    public String getKey(Integer length){

        StringBuilder stringBuilder = new StringBuilder();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(9));
        }


        return stringBuilder.toString();
    }
}
