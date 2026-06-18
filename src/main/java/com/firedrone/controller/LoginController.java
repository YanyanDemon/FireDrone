package com.firedrone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "msg", required = false) String msg,
                        Model model) {
        if (error != null) {
            model.addAttribute("msg", "账号或密码错误");
        } else if (msg != null) {
            model.addAttribute("msg", msg);
        }
        return "login";
    }
}
