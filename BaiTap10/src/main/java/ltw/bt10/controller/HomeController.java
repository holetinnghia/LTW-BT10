package ltw.bt10.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String root() { return "redirect:/user"; }

    @GetMapping("/user")
    public String userHome() { return "user/index"; }

    @GetMapping("/admin")
    public String adminHome() { return "admin/index"; }
}