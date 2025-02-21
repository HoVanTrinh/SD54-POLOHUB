package com.example.demo.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {
@GetMapping("/admin")
    public String viewAdminHome(Model model){
    return "/admin/index";
}
}
