package com.yunying.gh.service;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.yunying.gh.domain.Contribution;
import com.yunying.gh.domain.Developer;
import com.yunying.gh.domain.Repository;
import com.yunying.gh.mapper.ContributionMapper;
import com.yunying.gh.mapper.DeveloperMapper;
import com.yunying.gh.mapper.RepositoryMapper;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class GithubService {

    @Autowired
    private GitHub githubClient;

    @Autowired
    private DeveloperMapper developerMapper;

    @Autowired
    private RepositoryMapper repositoryMapper;

    @Autowired
    private ContributionMapper contributionMapper;

    // 获取仓库信息
    public GHRepository getRepositoryInfo(String owner, String repoName) throws IOException, IOException {
        return githubClient.getRepository(owner + "/" + repoName);
    }

    // 获取仓库的 Star 数量
    public int getRepositoryStars(String owner, String repoName) throws IOException {
        GHRepository repository = getRepositoryInfo(owner, repoName);
        return repository.getStargazersCount();
    }

    // 获取用户信息
    public String getUserInfo(String username) throws IOException {
        return githubClient.getUser(username).getLocation();
    }

    /**
     * 插入用户信息
     *
     * @param username
     * @return
     * @throws IOException
     */
    public boolean insertUser(String username) throws IOException {
        GHUser user = githubClient.getUser(username);
        Developer developer = new Developer();
        Integer devId = Math.toIntExact(user.getId());
        String devLogin = user.getLogin();
        String devName = user.getName();
        String avatar = user.getAvatarUrl();
        String home = String.valueOf(user.getHtmlUrl());
        String location = user.getLocation();
        String email = user.getEmail();
        String bio = user.getBio();
        Integer followers = user.getFollowers().size();
        Integer following = user.getFollows().size();

        developer.setDevId(devId);
        developer.setDevLogin(devLogin);
        developer.setDevName(devName);
        developer.setAvatar(avatar);
        developer.setHome(home);
        developer.setLocation(location);
        developer.setEmail(email);
        developer.setBio(bio);
        developer.setFollowers(followers);
        developer.setFollowing(following);

        return developerMapper.insert(developer) == 1;

    }


    /**
     * 插入仓库信息
     *
     * @param owner
     * @return
     * @throws IOException
     */
    @Transactional
    public boolean insertRepository(String owner) throws IOException {
        GHUser user = githubClient.getUser(owner);
        user.listRepositories().forEach(repository -> {
            Repository repo = new Repository();
            Integer repoId = Math.toIntExact(repository.getId());
            String repoName = repository.getName();
            String repoFullName = repository.getFullName();
            String repoHome = String.valueOf(repository.getHtmlUrl());
            String description = repository.getDescription();
            try {
                Integer ownerId = Math.toIntExact(repository.getOwner().getId());
                String language = JSONUtil.toJsonStr(repository.listLanguages());
                Integer watchCount = repository.getWatchersCount();
                Integer forkCount = repository.getForksCount();
                Integer prCount = repository.getPullRequests(GHIssueState.ALL).size();
                Integer starCount = repository.getStargazersCount();
                Integer issueCount = repository.getIssues(GHIssueState.ALL).size();
                repo.setRepoId(repoId);
                repo.setRepoName(repoName);
                repo.setRepoFullName(repoFullName);
                repo.setRepoHome(repoHome);
                repo.setDescription(description);
                repo.setOwnerId(ownerId);
                repo.setLanguage(language);
                repo.setWatchCount(watchCount);
                repo.setForkCount(forkCount);
                repo.setPrCount(prCount);
                repo.setStarCount(starCount);
                repo.setIssueCount(issueCount);
                int insert = repositoryMapper.insert(repo);
                if (insert != 1) {
                    throw new RuntimeException("insert repository failed");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        return true;
    }

    /**
     * 根据用户名插入贡献信息
     *
     * @param owner
     * @return
     * @throws IOException
     */
    public boolean insertContribution(String owner) throws IOException {
        GHUser user = githubClient.getUser(owner);
        Integer devId = Math.toIntExact(user.getId());
        Map<Integer, Map<String, ContributionCount>> repositoryContributions = new HashMap<>();

        user.listEvents().forEach(event -> {
            // 获取事件类型
            String eventType = String.valueOf(event.getType());

            // 获取贡献者 ID
            String contributorId = String.valueOf(user.getId()); // 用户 ID

            // 获取相关的仓库
            try {
                GHRepository repository = event.getRepository();
                Integer repoId = Math.toIntExact(repository.getId()); // 仓库 ID
                repositoryContributions.putIfAbsent(repoId, new HashMap<>());

                // 获取或创建贡献计数
                ContributionCount contributionCount = repositoryContributions.get(repoId)
                        .computeIfAbsent(contributorId, id -> new ContributionCount());

                // 根据事件类型更新贡献计数
                switch (eventType) {
                    case "PushEvent" -> contributionCount.commitCount++;
                    case "PullRequestEvent" -> contributionCount.prCount++;
                    case "IssuesEvent" -> contributionCount.issueCount++;
                    default -> {
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        // 输出结果
        repositoryContributions.forEach((repoId, contributions) -> {
            System.out.println("仓库 ID: " + repoId);
            contributions.forEach((contributorId, count) -> {
                Contribution contribution = new Contribution();
                System.out.println("  贡献者 ID: " + contributorId);
                System.out.println("    提交次数: " + count.commitCount);
                System.out.println("    PR 次数: " + count.prCount);
                System.out.println("    Issue 次数: " + count.issueCount);

                contribution.setDevId(devId);
                contribution.setRepoId(repoId);
                contribution.setCommitCount(count.commitCount);
                contribution.setPrCount(count.prCount);
                contribution.setIssueCount(count.issueCount);

                // 插入数据库
                contributionMapper.insert(contribution);

            });
        });

        return true;
    }

    // 内部类用于存储贡献计数
    private static class ContributionCount {
        int commitCount = 0;
        int prCount = 0;
        int issueCount = 0;
    }
}
