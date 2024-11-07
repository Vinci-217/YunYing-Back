package com.yunying.gh;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@MapperScan("com.yunying.gh.mapper")
public class GithubApplication {


    public static void main(String[] args) {
        SpringApplication.run(GithubApplication.class, args);
        System.out.println("Github Application started successfully!");
    }

}
