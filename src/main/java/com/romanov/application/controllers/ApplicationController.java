package com.romanov.application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/my-app")
public class ApplicationController {

    @GetMapping("/home/{id}")
    public String welcomePage(@PathVariable int id, Model model) {
        model.addAttribute("pageId", id);
        return "home";
    }

}
