package ltw.bt10.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/login") public String login(){ return "login"; }
    @GetMapping("/admin/home") public String admin(){ return "admin-home"; }
    @GetMapping("/user/home") public String user(){ return "user-home"; }
}