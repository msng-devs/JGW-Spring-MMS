package com.jaramgroupware.mms.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 컨테이너에서 어플리케이션 health check를 위해 사용하는 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@RestController
public class PingController {
    /**
     * 컨테이너에서 어플리케이션 health check를 위해 사용하는 함수
     * @return health check 통과시 PONG 리턴
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping(){
        return ResponseEntity.ok("PONG");
    }
}
