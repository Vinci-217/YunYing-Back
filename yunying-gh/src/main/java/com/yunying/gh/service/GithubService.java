package com.yunying.gh.service;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GithubService {

    @Autowired
    private GitHub githubClient;

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
}
