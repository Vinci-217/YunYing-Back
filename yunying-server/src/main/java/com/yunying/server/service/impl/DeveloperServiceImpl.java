package com.yunying.server.service.impl;

import com.yunying.server.domain.Developer;
import com.yunying.server.mapper.DeveloperMapper;
import com.yunying.server.service.IDeveloperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Developer> selectByField(String field, Integer page, Integer pageSize) {
        return developerMapper.selectByField(field, page, pageSize);
    }

    @Override
    public List<Developer> selectByNation(String nation, Integer page, Integer pageSize) {
        return developerMapper.selectByNation(nation, page, pageSize);
    }

    @Override
    public List<Developer> selectByFieldAndNation(String field, String nation, Integer page, Integer pageSize) {
        return developerMapper.selectByFieldAndNation(field, nation, page, pageSize);
    }

    @Override
    public List<Developer> selectByPage(Integer page, Integer pageSize) {
        int limit = pageSize;
        int offset = (page - 1) * pageSize;
        return developerMapper.selectByPage(limit, offset);
    }

}
