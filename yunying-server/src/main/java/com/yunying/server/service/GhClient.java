package com.yunying.server.service;

import com.yunying.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "yunying-gh")
public interface GhClient {

    @PostMapping("/github/insert/developer")
    int insertDeveloperInfo(@RequestParam("devLogin") String devLogin);
}
