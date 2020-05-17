package org.cherry.learn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tony on 2020/5/13.
 */

@Controller
public class HelloController {

    /*
        Main Page Message;
        Using git to control
     */
    @ResponseBody
    @RequestMapping("/")
    public String index(){
        return "Main Page";
    }

    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        return "Hello Spring Boot Burton SnowBoard Application";
    }

    @ResponseBody
    @RequestMapping("/again")
    public String again(){
        return "See Message Again!";
    }
}
