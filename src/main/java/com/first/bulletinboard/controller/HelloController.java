package com.first.bulletinboard.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Api(tags = {"Test"})
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class HelloController {
    @ApiOperation(value = "hello", notes = "hello 출력")
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
//        log.info("crontab 실행여부 확인");
        return ResponseEntity.ok().body("hello");
    }
}
