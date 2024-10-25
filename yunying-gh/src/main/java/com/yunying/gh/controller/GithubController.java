package com.yunying.gh.controller;

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
    public String getRepoInfo(@RequestParam String owner, @RequestParam String repoName) {
        try {
            GHRepository repo = gitHubService.getRepositoryInfo(owner, repoName);
            return "Repository: " + repo.getName() + ", Language: " + repo.getLanguage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    // 获取仓库的 Star 数量的 API
    @GetMapping("/repo/stars")
    public String getRepoStars(@RequestParam String owner, @RequestParam String repoName) {
        try {
            int stars = gitHubService.getRepositoryStars(owner, repoName);
            return "Stars: " + stars;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    // 获取用户信息的 API
    @GetMapping("/user")
    public String getUserInfo(@RequestParam String username) {
        try {
            return gitHubService.getUserInfo(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
