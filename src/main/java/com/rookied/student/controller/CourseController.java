package com.rookied.student.controller;

import com.alibaba.fastjson.JSONObject;
import com.rookied.student.service.CourseService;
import org.springframework.ui.Model;
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
    @GetMapping(value = "/course")
    public String course(@RequestParam(value ="cmaj",required = false,defaultValue = "计算机科学与技术")String cMaj,
                               @RequestParam(value ="cterm",required = false,defaultValue = "1")String cterm, Model model){
        Map<String,String> map;
        //System.out.println(cMaj+"----"+cterm);
        List<String> courses = courseService.findCourseByRedis("计算机科学与技术".equals(cMaj)?"计科":"会计", cterm);
        //System.out.println(courses);
        List<Map<String,String>> list = new ArrayList<>();
        String key = "coure";
        int index = 0;
        for (String course : courses) {
            map = new HashMap<>();
            map.put(key+index,courses.get(index));
            list.add(map);
            index++;
        }

       // map.put(cMaj+cterm,courses);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("cmaj", cMaj);
        jsonObject.put("cterm", cterm);
        jsonObject.put("data", courses);
        model.addAttribute("mm","hello");
        return jsonObject.toJSONString();

        //return courses;
    }
}
