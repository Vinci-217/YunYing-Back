package com.yunying.server.service.impl;

import cn.hutool.json.JSONUtil;
import com.yunying.server.domain.Developer;
import com.yunying.server.mapper.DeveloperMapper;
import com.yunying.server.service.IDeveloperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author vinci
 * @since 2024-10-31
 */
@Service
public class DeveloperServiceImpl extends ServiceImpl<DeveloperMapper, Developer> implements IDeveloperService {


    @Autowired
    private DeveloperMapper developerMapper;

    @CircuitBreaker(name = "query", fallbackMethod = "fallbackMethod")
    @RateLimiter(name = "query")
    @Override
    public List<Map<String, Object>> selectByField(String field, Integer page, Integer pageSize) {
        int limit = pageSize;
        int offset = (page - 1) * pageSize;
        return developerMapper.selectByField(field, limit, offset);
    }

    @CircuitBreaker(name = "query", fallbackMethod = "fallbackMethod")
    @RateLimiter(name = "query")
    @Override
    public List<Map<String, Object>> selectByNation(String nation, Integer page, Integer pageSize) {
        int limit = pageSize;
        int offset = (page - 1) * pageSize;
        return developerMapper.selectByNation(nation, limit, offset);
    }

    @CircuitBreaker(name = "query", fallbackMethod = "fallbackMethod")
    @RateLimiter(name = "query")
    @Override
    public List<Map<String, Object>> selectByFieldAndNation(String field, String nation, Integer page, Integer pageSize) {
        int limit = pageSize;
        int offset = (page - 1) * pageSize;
        return developerMapper.selectByFieldAndNation(field, nation, limit, offset);
    }

    @CircuitBreaker(name = "query", fallbackMethod = "fallbackMethodList")
    @RateLimiter(name = "query")
    @Override
    public List<Map<String, Object>> selectByPage(Integer page, Integer pageSize) {
        int limit = pageSize;
        int offset = (page - 1) * pageSize;
        return developerMapper.selectByPage(limit, offset);
    }

    @RateLimiter(name = "query")
    @Override
    public List<String> selectNation() {
        return developerMapper.selectNation();
    }

    @RateLimiter(name = "query")
    @Override
    public List<String> selectField() {
        return developerMapper.selectField();
    }

    @Override
    public Developer selectByDevId(Integer devId) {
        return developerMapper.selectById(devId);
    }

    @Override
    public List<Map<String, Object>> selectContribution(Integer devId) {
        return developerMapper.selectContribution(devId);
    }

    @Override
    public Map<String, Integer> selectLanguageByDevId(Integer devId) {
        // 获取所有的 JSON 字符串
        List<String> languagesJson = developerMapper.selectLanguageByDevId(devId);

        Map<String, Integer> languageStats = new HashMap<>();

        // 解析每个 JSON 字符串并统计语言数量
        for (String languageJson : languagesJson) {
            // 使用 Hutool 的 JSONUtil 将 JSON 字符串解析成 Map
            Map<String, Integer> languageMap = JSONUtil.toBean(languageJson, Map.class);

            // 遍历 map，累加统计
            for (Map.Entry<String, Integer> entry : languageMap.entrySet()) {
                languageStats.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
        }
        return languageStats;
    }

    public List<Map<String, Object>> fallbackMethodList(Throwable throwable) {
        return Collections.emptyList();
    }


}
