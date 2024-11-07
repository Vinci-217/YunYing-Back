package com.yunying.ai.controller;


import com.yunying.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/ai")
public class AIController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;

    // 历史消息列表
    static List<Message> historyMessage = new ArrayList<>();
    // 历史消息列表的最大长度
    static int maxLen = 10;


    // TODO: 流式响应和UTF-8编码的冲突尚未解决
    // TODO: 仅仅满足了单个用户的聊天，多用户的聊天还需要进一步设计
    @PostMapping(value = "/chat", produces = "text/plain; charset=UTF-8")
    public Result generation(String userInput) {

        // 发起聊天请求并处理响应
        String output = chatClient.prompt()
                .messages(historyMessage)
                .user(userInput)
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .call()
                .content();


        // 用户输入的文本是UserMessage
        historyMessage.add(new UserMessage(userInput));

        // 发给AI前对历史消息对列的长度进行检查
        if (historyMessage.size() > maxLen) {
            historyMessage = historyMessage.subList(historyMessage.size() - maxLen - 1, historyMessage.size());
        }

        return Result.success(output);
    }

    @PostMapping(value = "/report", produces = "text/plain; charset=UTF-8")
    public String getReport(@RequestParam("devLogin") String devLogin, @RequestParam("content") String content) {

        String userInput = "我是" + devLogin + "，请你针对Github的开源数据给我一个综合评估报告。我的数据如下：" +
                content;
        // 发起聊天请求并处理响应
        String output = chatClient.prompt()
                .messages()
                .user(userInput)
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .call()
                .content();


        return output;
    }

    @PostMapping(value = "/field", produces = "text/plain; charset=UTF-8")
    public String getField(@RequestParam("devLogin") String devLogin, @RequestParam("content") String content) {

        String userInput = "我是" + devLogin + "，请你根据下面内容预测我的开发领域。我的数据如下：" +
                content;
        // 发起聊天请求并处理响应
        String output = chatClient.prompt()
                .messages()
                .user(userInput)
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .call()
                .content();


        return output;
    }


}