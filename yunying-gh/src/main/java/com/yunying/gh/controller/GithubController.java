package com.yunying.gh.controller;

import com.yunying.common.utils.Result;
import com.yunying.gh.service.GithubService;
import org.kohsuke.github.GHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/github")
public class GithubController {

    @Autowired
    private GithubService gitHubService;

    // 获取仓库信息的 API
    @GetMapping("/repo")
    public Result<String> getRepoInfo(@RequestParam String owner, @RequestParam String repoName) {
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
    public Result<Integer> getRepoStars(@RequestParam String owner, @RequestParam String repoName) {
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
    public Result<String> getUserInfo(@RequestParam String username) {
        try {
            return Result.success(gitHubService.getUserInfo(username));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

//    @GetMapping("/user/repos/{username}")
//    public Result<String> getUserRepos(@PathVariable String username) throws IOException {
//        gitHubService.insertContribution(username);
//        return Result.success("success");
//    }

    @PostMapping("/insert/developer")
    public int insertDeveloperInfo(@RequestParam("devLogin") String devLogin) throws IOException {
        boolean user = gitHubService.insertUser(devLogin);
        boolean repo = gitHubService.insertRepository(devLogin);
        boolean contribution = gitHubService.insertContribution(devLogin);
        if (user && repo && contribution) {
            return 1;
        }
        return 0;
    }
//
//    /**
//     * 更新仓库信息的 API
//     *
//     * @return
//     */
//    @PostMapping("/update/repository")
//    public Result<String> updateRepoInfo() {
//
//    }
//
//    /**
//     * 更新开发者信息的 API
//     *
//     * @return
//     */
//    @PostMapping("/update/developer")
//    public Result<String> updateDeveloperInfo() {
//
//    }
//
//    /**
//     * 更新贡献信息的 API
//     *
//     * @return
//     */
//    @PostMapping("/update/contribution")
//    public Result<String> updateContributionInfo() {
//
//    }
//
//    /**
//     * 更新关注者信息的 API
//     *
//     * @return
//     */
//    @PostMapping("/update/follower")
//    public Result<String> updateFollowerInfo() {
//
//    }
//
//    /**
//     * 更新粉丝信息的 API
//     *
//     * @return
//     */
//    @PostMapping("/update/following")
//    public Result<String> updateFollowingInfo() {
//
//    }
//
//    /**
//     * 插入新的开发者
//     *
//     * @return
//     */
//    @PostMapping("/insert/developer")
//    public Result<String> insertDeveloperInfo() {
//
//    }


}
