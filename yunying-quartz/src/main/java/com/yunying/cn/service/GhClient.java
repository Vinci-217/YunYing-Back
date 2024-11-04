package com.yunying.cn.service;

import com.yunying.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "yunying-gh")
public interface GhClient {

    @PostMapping("/github/update/repository")
    Result<String> updateRepoInfo();

    @PostMapping("/github/update/developer")
    Result<String> updateDeveloperInfo();

    @PostMapping("/github/update/contribution")
    Result<String> updateContributionInfo();

    @PostMapping("/github/update/follower")
    Result<String> updateFollowerInfo();

    @PostMapping("/github/update/following")
    Result<String> updateFollowingInfo();

}
