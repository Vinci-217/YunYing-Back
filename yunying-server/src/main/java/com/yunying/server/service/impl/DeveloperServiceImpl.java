package com.yunying.server.service.impl;

import com.yunying.server.domain.Developer;
import com.yunying.server.mapper.DeveloperMapper;
import com.yunying.server.service.IDeveloperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vinci
 * @since 2024-10-31
 */
@Service
public class DeveloperServiceImpl extends ServiceImpl<DeveloperMapper, Developer> implements IDeveloperService {


    @Autowired
    private DeveloperMapper developerMapper;

    @Override
    public List<Map<String, Object>> selectByField(String field, Integer page, Integer pageSize) {
        int limit = pageSize;
        int offset = (page - 1) * pageSize;
        return developerMapper.selectByField(field, limit, offset);
    }

    @Override
    public List<Map<String, Object>> selectByNation(String nation, Integer page, Integer pageSize) {
        int limit = pageSize;
        int offset = (page - 1) * pageSize;
        return developerMapper.selectByNation(nation, limit, offset);
    }

    @Override
    public List<Map<String, Object>> selectByFieldAndNation(String field, String nation, Integer page, Integer pageSize) {
        int limit = pageSize;
        int offset = (page - 1) * pageSize;
        return developerMapper.selectByFieldAndNation(field, nation, limit, offset);
    }

    @Override
    public List<Map<String, Object>> selectByPage(Integer page, Integer pageSize) {
        int limit = pageSize;
        int offset = (page - 1) * pageSize;
        return developerMapper.selectByPage(limit, offset);
    }

    @Override
    public List<String> selectNation() {
        return developerMapper.selectNation();
    }

    @Override
    public List<String> selectField() {
        return developerMapper.selectField();
    }

}
