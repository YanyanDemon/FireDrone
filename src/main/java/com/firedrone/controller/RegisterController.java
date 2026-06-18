package com.firedrone.controller;

import com.firedrone.entity.UserEntity;
import com.firedrone.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private static final String ADMIN_INVITE = "FIREDRONE2026";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirm,
                           @RequestParam(defaultValue = "操作员") String role,
                           @RequestParam(required = false) String invite,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        if (username.isBlank() || username.length() < 2 || username.length() > 20) {
            model.addAttribute("msg", "用户名长度需在2-20个字符之间");
            return "register";
        }
        if (password.length() < 6) {
            model.addAttribute("msg", "密码长度不能少于6位");
            return "register";
        }
        if (!password.equals(confirm)) {
            model.addAttribute("msg", "两次输入的密码不一致");
            return "register";
        }
        if (!"管理员".equals(role) && !"操作员".equals(role)) {
            role = "操作员";
        }
        if ("管理员".equals(role) && !ADMIN_INVITE.equals(invite)) {
            model.addAttribute("msg", "管理员邀请码不正确");
            return "register";
        }
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("msg", "用户名已被占用");
            return "register";
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("msg", "注册成功，请登录");
        return "redirect:/login";
    }
}
