package com.yunying.cn.controller;


import com.yunying.cn.entity.QuartzJob;
import com.yunying.cn.service.QuartzJobService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quartz")
public class QuartzController  {

    @Autowired
    private QuartzJobService quartzJobService ;

    @GetMapping("/job/{id}")
    public QuartzJob getById(@PathVariable Integer id){
        return quartzJobService.getById(id) ;
    }

    @PostMapping("/job")
    public Integer insert(@RequestBody QuartzJob quartzJob){
        return quartzJobService.insert(quartzJob) ;
    }

    @PutMapping("/job")
    public Integer update(@RequestBody QuartzJob quartzJob){
        return quartzJobService.update(quartzJob) ;
    }

    @PutMapping("/job/pause/{id}")
    public void pause(@PathVariable("id") Integer id) {
        quartzJobService.pause(id);
    }

    @PutMapping("/job/resume/{id}")
    public void resume(@PathVariable("id") Integer id) {
        quartzJobService.resume(id) ;
    }

    @GetMapping("/job/runOnce/{id}")
    public void runOnce(@PathVariable("id") Integer id) {
        quartzJobService.runOnce(id) ;
    }
}