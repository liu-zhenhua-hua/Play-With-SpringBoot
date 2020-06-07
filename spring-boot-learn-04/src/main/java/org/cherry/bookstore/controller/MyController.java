package org.cherry.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by tony on 2020/6/5.
 */

@Controller
public class MyController {

    @PostMapping(value="/user/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Map<String,Object> map, HttpSession session){

        if(!StringUtils.isEmpty(username) && "123456".equals(password)){
            session.setAttribute("loginUserName",username);
            return "redirect:/main.html";
        }else{
            map.put("msg","Login Failed");
            return "login";
        }

    }

}
