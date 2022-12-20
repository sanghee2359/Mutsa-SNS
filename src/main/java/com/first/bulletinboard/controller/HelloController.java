package com.first.bulletinboard.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Api(tags = {"Test"})
@RestController
@RequestMapping("/api/v1")
public class HelloController {
    @ApiOperation(value = "hello", notes = "hello 출력")
    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello");
    }
}
