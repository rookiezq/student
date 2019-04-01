package com.rookied.student.controller;

import com.alibaba.fastjson.JSONObject;
import com.rookied.student.bean.Course;
import com.rookied.student.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desciption:
 * @author: Demon
 * @version: 1.0 2019-03-30 14:05
 **/
@RestController
public class CourseController {
    @Resource
    private CourseService courseService;

    /**
     * 获取一个某个专业某一学期的课程
     * 默认获取计科 第一学期
     * @return
     */
    @PostMapping(value = "/course")
    public String course(@RequestParam(value ="cmaj",required = false,defaultValue = "计科")String cMaj,
                         @RequestParam(value ="cterm",required = false,defaultValue = "1")int cterm){
        Map<String,String> map;
        List<String> courses = courseService.findCourseByRedis(cMaj, cterm);
        List<Map<String,String>> list = new ArrayList<>();
        String key = "coure";
        int index = 0;
        for (String cours : courses) {
            map = new HashMap<>();
            map.put(key+index,courses.get(index));
            list.add(map);
            index++;
        }

       // map.put(cMaj+cterm,courses);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", list.size());
        jsonObject.put("data", list);
        return jsonObject.toJSONString();
    }
}
