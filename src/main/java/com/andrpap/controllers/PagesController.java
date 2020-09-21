package com.andrpap.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.andrpap.models.PageRepository;
import com.andrpap.models.entities.Page;


@Controller
@RequestMapping("/")
public class PagesController {
	
	@Autowired
    private PageRepository pgrp;

    @GetMapping
    public String home(Model themodel) {
        
        Page page = pgrp.findBySlug("home");
        themodel.addAttribute("page", page);
        
        return "page";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //emfanisi olwv twn selidwn
    
    @GetMapping("/{slug}")
    public String page(@PathVariable String slug, Model themodel) {
        
        Page page = pgrp.findBySlug(slug);

        if (page == null) {
            return "redirect:/";
        }
        
        themodel.addAttribute("page", page);
        
        return "page";
    }

}
