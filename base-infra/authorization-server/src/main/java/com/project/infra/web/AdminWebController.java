package com.project.infra.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminWebController {

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
