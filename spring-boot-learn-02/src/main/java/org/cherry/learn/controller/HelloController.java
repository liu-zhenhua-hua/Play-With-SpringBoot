package org.cherry.learn.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tony on 2020/5/27.
 */

@RestController
public class HelloController {

    /*
        Testing YAML port configuration
     */
    @RequestMapping("/show")
    public String showMessage(){

        return "Hello YAML Training Lesson ";
    }
}
