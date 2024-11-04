package com.yunying.ai.config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
public class AIConfiguration {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem("你将作为一个对Github开发者信息和数据进行评估的专家，" +
                        "会对开发者的信息进行评估，并给出评估报告。")
                .build();
    }
}
