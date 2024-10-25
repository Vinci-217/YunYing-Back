package com.yunying.gh.controller;

import com.yunying.common.utils.Result;
import com.yunying.gh.service.GithubService;
import org.kohsuke.github.GHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/github")
public class GithubController {

    @Autowired
    private GithubService gitHubService;

    // 获取仓库信息的 API
    @GetMapping("/repo")
    public Result getRepoInfo(@RequestParam String owner, @RequestParam String repoName) {
        try {
            GHRepository repo = gitHubService.getRepositoryInfo(owner, repoName);
            String result = "Repository: " + repo.getName() + ", Language: " + repo.getLanguage() + ", Stars: " + repo.getStargazersCount();
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    // 获取仓库的 Star 数量的 API
    @GetMapping("/repo/stars")
    public Result getRepoStars(@RequestParam String owner, @RequestParam String repoName) {
        try {
            int stars = gitHubService.getRepositoryStars(owner, repoName);
            return Result.success(stars);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    // 获取用户信息的 API
    @GetMapping("/user")
    public Result getUserInfo(@RequestParam String username) {
        try {
            return Result.success(gitHubService.getUserInfo(username));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
}
