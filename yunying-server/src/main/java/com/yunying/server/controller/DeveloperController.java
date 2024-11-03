package com.yunying.server.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunying.common.utils.Result;
import com.yunying.server.domain.Developer;
import com.yunying.server.service.IDeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Result<String> selectAIReport(@PathVariable("dev_id") Integer dev_id) {
        QueryWrapper<Developer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dev_id", dev_id).select("profile");
        Developer developer = developerService.getOne(queryWrapper);
        return Result.success(developer.getProfile());
    }


}
