package com.project.oop.PMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("api/web/projects")
public class ProjectHomeController {

    @GetMapping()
    public String showHomePage() {
        return "index"; // Tên file HTML trong thư mục templates
    }

}
