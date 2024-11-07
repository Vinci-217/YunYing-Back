package com.yunying.gh.controller;

import com.yunying.common.utils.Result;
import com.yunying.gh.service.GithubService;
import com.yunying.gh.service.IDeveloperService;
import com.yunying.gh.service.IRepositoryService;
import org.kohsuke.github.GHRepository;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/github")
public class GithubController {

    @Autowired
    private GithubService gitHubService;

    @Autowired
    private IDeveloperService developerService;

    @Autowired
    private IRepositoryService repositoryService;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedissonClient redissonClient;

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

    /**
     * 插入或者更新开发者信息的 API
     *
     * @param devLogin
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "bye.queue", durable = "true"),
            exchange = @Exchange(name = "amq.direct"),
            key = "byekey"
    ))
    @Transactional
    public void insertDeveloperInfo(String devLogin) throws IOException, NoSuchFieldException, IllegalAccessException {
        System.out.println("收到消息：" + devLogin);


        RBucket<String> bucket = redissonClient.getBucket("work");
        bucket.set("true", 4, TimeUnit.MINUTES);  // 设置键值对

        boolean user = gitHubService.insertOrUpdateDeveloper(devLogin);

        if (user) {
            rabbitTemplate.convertAndSend("amq.direct", "hellokey", devLogin);
        }

        gitHubService.insertOrUpdateFollower(devLogin);
        gitHubService.insertOrUpdateFollowing(devLogin);

        // 计算开发者的粉丝得分
        developerService.calculateFollowersScore(devLogin);
        // 预测开发者的国家
        developerService.propagateNation(devLogin);
        // 计算开发者的Talentrank
        developerService.calculateTalentRank(devLogin);

        // 插入或更新开发者的报告
        developerService.setReport(devLogin);

        // 插入或更新开发者的仓库信息和贡献信息
        boolean repo = gitHubService.insertOrUpdateRepository(devLogin);


        boolean contribution = gitHubService.insertOrUpdateContribution(devLogin);

        if (user && repo && contribution) {
            System.out.println("插入新用户完成！");
            bucket.set("false");
//            rabbitTemplate.convertAndSend("amq.direct", "finishkey", devLogin);
        }
    }


    /**
     * 更新仓库信息的 API
     *
     * @return
     */
    @PostMapping("/update/developer")
    public Result<String> updateDeveloperInfo() {

        return Result.success("success");
    }


}
