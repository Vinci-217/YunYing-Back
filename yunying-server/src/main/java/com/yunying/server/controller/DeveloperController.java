package com.yunying.server.controller;


import com.yunying.common.utils.Result;
import com.yunying.server.domain.Developer;
import com.yunying.server.service.IDeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
    public Result<List<Map<String, Object>>> select(
            @RequestParam("field") String field, @RequestParam("nation") String nation,
            @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize){

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
     * @return
     */
    @GetMapping("/select/nation")
    public Result<List<String>> selectNation(){
         List<String> nations = developerService.selectNation();
         return Result.success(nations);
    }

    /**
     * 获取所有领域
     * @return
     */
    @GetMapping("/select/field")
    public Result<List<String>> selectField(){
        List<String> fields = developerService.selectField();
        return Result.success(fields);
    }


}
