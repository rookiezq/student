package com.rookied.student.controller;

import com.rookied.student.service.AlyPreService;
import com.rookied.student.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @desciption:
 * @author: Demon
 * @version: 1.0 2019-03-27 15:41
 **/
@Controller
@RequestMapping("")
public class PageController {

    @Resource
    private AlyPreService alyPreService;
    @Resource
    private CourseService courseService;

    @RequestMapping("/index")
    public String demo(){
        return "index";
    }

    @RequestMapping("/score")
    public String score(){
        return "score";
    }

    @RequestMapping("/analyse")
    public String analyse(String maj, Model model) {
        alyPreService.analyse(maj);
        if (!"2".equals(maj)) {
            maj = "0".equals(maj) ? "计科" : "会计";
            String src1 = "/img/" + maj + "毕业去向和平均分.jpg";
            String src2 = "/img/" + maj + "毕业去向比例.jpg";
            model.addAttribute("src1", src1);
            model.addAttribute("src2", src2);
        } else {
            String src1 = "/img/计科和会计的平均分.jpg";
            String src2 = "/img/计科和会计去向比例.jpg";
            String src3 = "/img/毕业去向和平均分.jpg";
            String src4 = "/img/毕业去向和挂科数.jpg";
            model.addAttribute("src1", src1);
            model.addAttribute("src2", src2);
            model.addAttribute("src3", src3);
            model.addAttribute("src4", src4);
        }
        return "analyse";
    }

    @RequestMapping("/predict")
    public String predict(String maj, Model model){
        String major = "0".equals(maj) ? "计科" : "会计";
        List<String> courses = courseService.findCourseByRedis(major, "主要课程");
        model.addAttribute("courses",courses);
        model.addAttribute("maj",maj);
        return "predict";
    }

    @RequestMapping("/predictScore")
    public String predictScore(String maj,String course, Model model){
        String data = alyPreService.predict(maj,course).split("]")[0].split("\\[")[1];
        List<Double> list = new ArrayList<>(3);
        list.add(Double.valueOf(data.split(" ")[0]));
        list.add(Double.valueOf(data.split(" ")[1]));
        list.add(Double.valueOf(data.split(" ")[2]));
        String predictSrc = "/img/predict.jpg";
        model.addAttribute("predict",predictSrc);
        model.addAttribute("predictList",list);
        return predict(maj,model);
    }
}
