package com.rookied.student.controller;

import com.alibaba.fastjson.JSONObject;
import com.rookied.student.service.ScoreService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desciption:
 * @author: Demon
 * @version: 1.0 2019-03-30 22:36
 **/
@RestController
public class ScoreController {
    @Resource
    private ScoreService scoreService;

    /**
     * 获取一个某个专业某一学期所有课程的成绩
     * 默认获取计科 第一学期
     */
    @PostMapping(value = "/score")
    public String score(@RequestParam(value ="cmaj",required = false,defaultValue = "计科")String cMaj,
                         @RequestParam(value ="cterm",required = false,defaultValue = "1")int cterm){
        Map<String,List<String>> map = scoreService.findScoreByTermMajor(cMaj, cterm);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", cMaj+cterm+"所有的成绩");
        jsonObject.put("count", 10);
        jsonObject.put("data", map);
        return jsonObject.toJSONString();
    }

}
