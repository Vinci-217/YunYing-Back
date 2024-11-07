package com.yunying.gh.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "yunying-ai")
public interface AIClient {

    @PostMapping("/ai/report")
    String getReport(@RequestParam("devLogin") String devLogin, @RequestParam("content") String content);

    @PostMapping("/ai/field")
    String getField(@RequestParam("devLogin") String devLogin, @RequestParam("content") String content);

}
