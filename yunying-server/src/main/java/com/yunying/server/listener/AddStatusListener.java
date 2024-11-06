package com.yunying.server.listener;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class AddStatusListener {

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "hello.queue", durable = "true"),
//            exchange = @Exchange(name = "amq.direct"),
//            key = "hellokey"
//    ))
//    public void handlePaySuccessMessage(String devLogin) {
//        System.out.println("收到来自 B 的通知，开发者 " + devLogin + " 处理成功");
//
//        // 当消息到达时，通知等待中的 future
//        CompletableFuture<String> future = messageFutureMap.get(devLogin);
//        if (future != null) {
//            // 完成通知
//            future.complete("success");
//        }
//    }
}
