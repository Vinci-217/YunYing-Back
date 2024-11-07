package com.yunying.gh.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunying.gh.domain.*;
import com.yunying.gh.mapper.*;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private FollowerMapper followerMapper;

    @Autowired
    private FollowingMapper followingMapper;

    @Autowired
    private IRepositoryService repositoryService;

    @Autowired
    private IContributionService contributionService;


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
    public boolean insertOrUpdateDeveloper(String username) throws IOException {
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
        System.out.println("insert user success");

        return developerMapper.insertOrUpdate(developer);

    }


    /**
     * 插入仓库信息
     *
     * @param owner
     * @return
     * @throws IOException
     */
    @Transactional
    public boolean insertOrUpdateRepository(String owner) throws IOException {
        GHUser user = githubClient.getUser(owner);
        Map<String, GHRepository> repositories = user.getRepositories();
        for (GHRepository repository : repositories.values()) {
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
                boolean insert = repositoryMapper.insertOrUpdate(repo);

                repositoryService.calculateImportanceScore(repo);

                if (!insert) {
                    throw new RuntimeException("insert repository failed");
                }
            } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("insert repository success");
        return true;
    }

    /**
     * 根据用户名插入贡献信息
     *
     * @param owner
     * @return
     * @throws IOException
     */
    public boolean insertOrUpdateContribution(String owner) throws IOException {
        GHUser user = githubClient.getUser(owner);
        Integer devId = Math.toIntExact(user.getId());
        Map<Integer, Map<String, ContributionCount>> repositoryContributions = new HashMap<>();

        List<GHEventInfo> eventInfoList = user.listEvents().toList();
        System.out.println("事件列表大小: " + eventInfoList.size());

        for (GHEventInfo eventInfo : eventInfoList) {
            // 获取事件类型
            String eventType = String.valueOf(eventInfo.getType());
//            System.out.println("事件类型: " + eventType);

            // 获取贡献者 ID
            String contributorId = String.valueOf(user.getId()); // 用户 ID

            // 获取相关的仓库
            try {
                GHRepository repository = eventInfo.getRepository();
                Integer repoId = Math.toIntExact(repository.getId()); // 仓库 ID
                repositoryContributions.putIfAbsent(repoId, new HashMap<>());

                // 获取或创建贡献计数
                ContributionCount contributionCount = repositoryContributions.get(repoId)
                        .computeIfAbsent(contributorId, id -> new ContributionCount());

                // 根据事件类型更新贡献计数
                switch (eventType) {
                    case "PUSH" -> contributionCount.commitCount++;
                    case "PULL_REQUEST" -> contributionCount.prCount++;
                    case "ISSUES" -> contributionCount.issueCount++;
                    default -> {
                    }
                }
            } catch (IOException e) {
                if (e.getMessage().contains("404")) {
                    System.out.println("仓库不存在或已删除，跳过该事件");
                    continue; // 跳过当前事件
                } else {
                    // 如果是其他IOException，则重新抛出
                    throw new RuntimeException("无法处理仓库信息", e);
                }
            }
        }


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
                contributionMapper.insertOrUpdate(contribution);

                try {
                    contributionService.calculateContributionScore(contribution);
                    contributionService.predictField(contribution);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException("贡献度插入失败");
                }


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

    /**
     * 插入用户的所有粉丝
     *
     * @param owner
     * @return
     * @throws IOException
     */
    public boolean insertOrUpdateFollower(String owner) throws IOException {
        GHUser user = githubClient.getUser(owner);
        user.getFollowers().forEach(follower -> {
            try {
                Follower followerEntity = new Follower();

                Integer devId = Math.toIntExact(follower.getId());
                String devLogin = follower.getLogin();
                String devName = follower.getName();
                if (devName == null) {
                    devName = devLogin;
                }
                String avatar = follower.getAvatarUrl();
                Integer followingId = Math.toIntExact(user.getId());
                String location = follower.getLocation();

                followerEntity.setDevId(devId);
                followerEntity.setDevLogin(devLogin);
                followerEntity.setDevName(devName);
                followerEntity.setAvatar(avatar);
                followerEntity.setFollowingId(followingId);
                followerEntity.setLocation(location);


//                System.out.println("粉丝 ID: " + devId);
//                System.out.println("粉丝登录名: " + devLogin);
//                System.out.println("粉丝昵称: " + devName);
//                System.out.println("粉丝头像: " + avatar);
//                System.out.println("关注者 ID: " + followingId);

                // 根据 dev_id 和 following_id 查询是否已经存在相同的记录
                QueryWrapper<Follower> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("dev_id", devId)
                        .eq("following_id", followingId);

                Follower existingFollower = followerMapper.selectOne(queryWrapper);

                if (existingFollower != null) {
                    System.out.println("该粉丝已经存在，跳过该粉丝");
                } else {
                    followerMapper.insert(followerEntity);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        System.out.println("插入粉丝成功");
        return true;
    }

    /**
     * 插入用户关注的用户
     *
     * @param owner
     * @return
     * @throws IOException
     */
    public boolean insertOrUpdateFollowing(String owner) throws IOException {
        GHUser user = githubClient.getUser(owner);
        user.getFollows().forEach(following -> {

            try {
                Following followingEntity = new Following();

                Integer devId = Math.toIntExact(following.getId());
                String devLogin = following.getLogin();
                String devName = following.getName();
                if (devName == null) {
                    devName = devLogin;
                }
                String avatar = following.getAvatarUrl();
                Integer followerId = Math.toIntExact(user.getId());
                String location = following.getLocation();

                followingEntity.setDevId(devId);
                followingEntity.setDevLogin(devLogin);
                followingEntity.setDevName(devName);
                followingEntity.setAvatar(avatar);
                followingEntity.setFollowerId(followerId);
                followingEntity.setLocation(location);

//                System.out.println("关注者 ID: " + devId);
//                System.out.println("关注者登录名: " + devLogin);
//                System.out.println("关注者昵称: " + devName);
//                System.out.println("关注者头像: " + avatar);
//                System.out.println("关注者 ID: " + followerId);

                // 根据 dev_id 和 follower_id 查询是否已经存在相同的记录
                QueryWrapper<Following> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("dev_id", devId)
                        .eq("follower_id", followerId);

                Following existingFollowing = followingMapper.selectOne(queryWrapper);

                if (existingFollowing != null) {
                    System.out.println("该关注者已经存在，跳过该关注者");
                } else {
                    followingMapper.insert(followingEntity);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        System.out.println("插入关注成功");
        return true;
    }
}
