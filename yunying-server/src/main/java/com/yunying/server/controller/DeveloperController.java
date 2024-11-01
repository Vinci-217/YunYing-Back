package com.yunying.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunying.common.utils.Result;
import com.yunying.server.domain.Developer;
import com.yunying.server.service.IDeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
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

    /**
     * 根据领域和国家查询开发者列表
     * @param field
     * @param nation
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/select/fieldAndNation")
    public Result<List<Developer>> select(
            @RequestParam("field") String field, @RequestParam("nation") String nation,
            @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize){

        // 1. 只根据field查询
        if (field != null && !field.trim().isEmpty()) {
            return Result.success(developerService.selectByField(field, page, pageSize));
        }
        // 2. 只根据nation查询
        if (nation != null && !nation.trim().isEmpty()) {
            return Result.success(developerService.selectByNation(nation, page, pageSize));
        }
        // 3. 只根据page和pageSize查询
        if (page != null && pageSize != null) {
            return Result.success(developerService.selectByPage(page, pageSize));
        }
        // 3. 根据field和nation查询
        return Result.success(developerService.selectByFieldAndNation(field, nation, page, pageSize));

    }


}
