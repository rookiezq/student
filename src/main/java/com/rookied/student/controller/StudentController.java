package com.rookied.student.controller;

import com.alibaba.fastjson.JSONObject;
import com.rookied.student.service.CourseService;
import com.rookied.student.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desciption:
 * @author: Demon
 * @version: 1.0 2019-03-31 09:39
 **/
@RestController
public class StudentController {
    @Resource
    private StudentService studentService;
    @Resource
    private CourseService courseService;

    @GetMapping(value = "/s")
    public String score(@RequestParam("page") Integer page,
                        @RequestParam("limit") Integer limit,
                        @RequestParam(value = "stuid", required = false, defaultValue = "") String stuid,
                        @RequestParam(value = "cmaj", required = false, defaultValue = "计算机科学与技术") String cMaj,
                        @RequestParam(value = "cterm", required = false, defaultValue = "1") int cterm) {
        System.out.println("page:"+page+"limit:"+limit+"stuid:"+stuid+"cmaj:"+cMaj+"cterm"+cterm);
        List<Map<String, String>> list = studentService.findAllScore(page,limit,stuid, cterm, cMaj);
        //分页
        List<Map<String, String>> pageList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < page * limit; i++) {
            if (list == null ||  i >= list.size()) {
                break;
            }
            pageList.add(list.get(i));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("stuList", pageList);
        List<String> courses = courseService.findCourseByRedis("计算机科学与技术".equals(cMaj)?"计科":"会计", cterm);
        map.put("courses", courses);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", stuid + cMaj + cterm + "所有的成绩");
        jsonObject.put("count", list.size());
        jsonObject.put("data", map);
        return jsonObject.toJSONString();
    }

}
