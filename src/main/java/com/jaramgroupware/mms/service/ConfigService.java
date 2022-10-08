package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.config.Config;
import com.jaramgroupware.mms.domain.config.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ConfigService {
    private final ConfigRepository configRepository;

    @Transactional
    public void add(String key,String val){
        configRepository.save(Config.builder()
                .name(key)
                .val(val)
                .build());
    }

    @Transactional(readOnly = true)
    public String find(String key){
        return configRepository.findConfigByName(key).getName();
    }
}
