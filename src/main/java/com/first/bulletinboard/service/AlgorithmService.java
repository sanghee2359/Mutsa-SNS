package com.first.bulletinboard.service;

import org.springframework.stereotype.Service;

@Service
public class AlgorithmService {
    public int sumOfDigit(int num) {
        int answer = 0;
        while(num >0) {
            answer += num%10;
            num = num/10;
        }
        return answer;
    }
}
