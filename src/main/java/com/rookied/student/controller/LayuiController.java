package com.rookied.student.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @desciption:
 * @author: Demon
 * @version: 1.0 2019-03-27 15:41
 **/
@Controller
@RequestMapping("/layui")
public class LayuiController {

    @RequestMapping("/index")
    public String demo(){
        return "index";
    }

    @RequestMapping("/score")
    public String score(){
        return "score";
    }
}
