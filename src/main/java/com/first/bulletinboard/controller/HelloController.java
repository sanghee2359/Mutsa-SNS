package com.first.bulletinboard.controller;

import com.first.bulletinboard.service.AlgorithmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Api(tags = {"Test"})
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class HelloController {
    private final AlgorithmService algorithmService;
    @ApiOperation(value = "hello", notes = "hello 출력")
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
//        log.info("crontab 실행여부 확인");
        return ResponseEntity.ok().body("정상희");
    }
    @GetMapping("/hello/{num}")
    public ResponseEntity<Integer> SumOfDigit(@PathVariable int num){
        int answer = algorithmService.sumOfDigit(num);
        return ResponseEntity.ok().body(answer);
    }
}
