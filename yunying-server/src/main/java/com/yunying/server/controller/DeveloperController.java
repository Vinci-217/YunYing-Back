package com.yunying.server.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunying.common.utils.Result;
import com.yunying.server.domain.Developer;
import com.yunying.server.listener.AddStatusListener;
import com.yunying.server.service.GhClient;
import com.yunying.server.service.IDeveloperService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.annotation.PostConstruct;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author vinci
 * @since 2024-10-31
 */
@RestController
@RequestMapping("/developer")
public class DeveloperController {

    @Autowired
    private IDeveloperService developerService;


    @Autowired
    private GhClient ghClient;

    @Autowired
    private AddStatusListener addStatusListener;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Autowired
    private RedissonClient redissonClient;

    private static final ConcurrentHashMap<String, CompletableFuture<String>> messageFutureMap = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, CompletableFuture<String>> messageReceivedMap = new ConcurrentHashMap<>();


    @PostConstruct
    public void init() {
        // 在Bean初始化之后执行
        System.out.println("Bean 初始化之后的操作");
        // 清除Redis缓存
        RBucket<Object> work = redissonClient.getBucket("work");
        work.set("false");

        // 清除队列缓存
        while (true) {
            // 尝试从队列中获取消息
            Object message1 = rabbitTemplate.receiveAndConvert("hello.queue");
            Object message2 = rabbitTemplate.receiveAndConvert("bye.queue");
            if (message1 == null && message2 == null) {
                // 如果没有消息，说明队列已经空了，跳出循环
                break;
            }
        }

        System.out.println("队列  已被清空");
    }

    /**
     * 根据领域和国家查询开发者列表
     *
     * @param field
     * @param nation
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/select/fieldAndNation")
    @CrossOrigin("http://localhost:3000")
    public Result<List<Map<String, Object>>> select(
            @RequestParam("field") String field, @RequestParam("nation") String nation,
            @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {

        // 1. 只根据field查询
        if (field != null && !field.trim().isEmpty()) {
            System.out.println("field:" + field);
            return Result.success(developerService.selectByField(field, page, pageSize));
        }
        // 2. 只根据nation查询
        if (nation != null && !nation.trim().isEmpty()) {
            System.out.println("nation:" + nation);
            return Result.success(developerService.selectByNation(nation, page, pageSize));
        }
        // 3. 只根据page和pageSize查询
        if (page != null && pageSize != null) {
            System.out.println("page:" + page + ",pageSize:" + pageSize);
            return Result.success(developerService.selectByPage(page, pageSize));
        }
        // 3. 根据field和nation查询
        System.out.println("hello");
        return Result.success(developerService.selectByFieldAndNation(field, nation, page, pageSize));

    }

    /**
     * 获取所有国家
     *
     * @return
     */
    @GetMapping("/select/nation")
    @CrossOrigin("http://localhost:3000")
    public Result<List<String>> selectNation() {
        List<String> nations = developerService.selectNation();
        return Result.success(nations);
    }

    /**
     * 获取所有领域
     *
     * @return
     */
    @GetMapping("/select/field")
    @CrossOrigin("http://localhost:3000")
    public Result<List<String>> selectField() {
        List<String> fields = developerService.selectField();
        return Result.success(fields);
    }

    /**
     * 根据dev_id查询开发者信息
     *
     * @param dev_id
     * @return
     */
    @GetMapping("/select/{dev_id}")
    @CrossOrigin("http://localhost:3000")
    public Result<Map<String, Object>> selectDeveloper(@PathVariable("dev_id") Integer dev_id) {
        Developer developer = developerService.selectByDevId(dev_id);

        // 使用 Hutool 转换对象为 Map，并排除 "profile" 属性
        Map<String, Object> developerMap = BeanUtil.beanToMap(developer,
                new HashMap<>(),
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreProperties("profile"));

        return Result.success(developerMap);

    }

    /**
     * 根据dev_id查询AI报告
     *
     * @param dev_id
     * @return
     */
    @GetMapping("/select/ai-report/{dev_id}")
    @CrossOrigin("http://localhost:3000")
    public Result<String> selectAIReport(@PathVariable("dev_id") Integer dev_id) {
        QueryWrapper<Developer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dev_id", dev_id).select("profile");
        Developer developer = developerService.getOne(queryWrapper);
        return Result.success(developer.getProfile());
    }

    /**
     * 根据dev_id查询贡献项目列表
     *
     * @param dev_id
     * @return
     */
    @GetMapping("select/contribution/{dev_id}")
    @CrossOrigin("http://localhost:3000")
    public Result<List<Map<String, Object>>> selectContribution(@PathVariable("dev_id") Integer dev_id) {
        List<Map<String, Object>> contribution = developerService.selectContribution(dev_id);
        return Result.success(contribution);
    }

    @PostMapping("/insert")
    @CrossOrigin("http://localhost:3000")
    @RateLimiter(name = "myServiceRateLimiter", fallbackMethod = "rateLimiterFallback")
    public Result<String> insert(@RequestBody Map<String, Object> dev) throws ExecutionException, InterruptedException, TimeoutException {

        System.out.println("接收到请求：" + dev);
        String devLogin = (String) dev.get("devLogin");

        RBucket<String> bucket = redissonClient.getBucket("work");
        if (bucket.get() != null && bucket.get().equals("true")) {
            return Result.error("服务器繁忙，请稍后重试");
        }
        // 发送插入请求
        rabbitTemplate.convertAndSend("amq.direct", "byekey", devLogin);

        String message = waitForNotification(devLogin);

        Developer finlaDeveloper = developerService.getOne(new QueryWrapper<Developer>().eq("dev_login", devLogin));


        if (message != null && message.equals("success")) {
            return Result.success(String.valueOf(finlaDeveloper.getDevId()));
        }


        return Result.error("插入失败");

    }

    private String waitForNotification(String devLogin) throws InterruptedException, ExecutionException, TimeoutException {
        // 创建一个新的 CompletableFuture 用来等待消息
        CompletableFuture<String> future = new CompletableFuture<>();

        // 将 future 存储到 messageFutureMap 中，以 devLogin 作为键
        messageFutureMap.put(devLogin, future);

        System.out.println("等待通知，devLogin=" + devLogin);

        // 阻塞当前线程，等待最大30秒，直到 CompletableFuture 完成
        return future.get(30, TimeUnit.SECONDS);  // 可以根据实际需求调整等待时间
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "hello.queue", durable = "true"),
            exchange = @Exchange(name = "amq.direct"),
            key = "hellokey"
    ))
    public void handlePaySuccessMessage(String devLogin) {

        System.out.println("收到通知，devLogin=" + devLogin);
        // 获取对应 devLogin 的 CompletableFuture 对象
        CompletableFuture<String> future = messageFutureMap.get(devLogin);

        if (future != null) {
            // 完成通知
            future.complete("success");
        } else {
            System.out.println("未找到对应的任务，devLogin=" + devLogin);
        }
    }

    // 限流回退方法
    public Result<String> rateLimiterFallback(Throwable t) {
        return Result.error("请求次数过多，请稍后重试");
    }

    /**
     * 根据dev_id查询语言分布
     *
     * @param devId
     * @return
     */
    @GetMapping("/select/language/{devId}")
    @CrossOrigin("http://localhost:3000")
    public Result<Map<String, Integer>> selectLanguage(@PathVariable("devId") Integer devId) {

        Map<String, Integer> language = developerService.selectLanguageByDevId(devId);
        return Result.success(language);
    }

    @GetMapping("/query/{devLogin}")
    @CrossOrigin("http://localhost:3000")
    public Result<Integer> queryDeveloper(@PathVariable("devLogin") String devLogin) {
        QueryWrapper<Developer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dev_login", devLogin);
        Developer developer = developerService.getOne(queryWrapper);
        if (developer == null) {
            return Result.error("未找到该开发者");
        }
        return Result.success(developer.getDevId());
    }
}
