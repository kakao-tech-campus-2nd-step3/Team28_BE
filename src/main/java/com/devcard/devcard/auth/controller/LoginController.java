package com.devcard.devcard.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("gitHubLoginController")
public class LoginController {

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("/redirect")
    public String redirectPage() {
        return "redirect";
    }
}
