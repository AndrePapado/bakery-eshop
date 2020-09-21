package com.andrpap.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.andrpap.models.PageRepository;
import com.andrpap.models.entities.Page;

@Controller
@RequestMapping("/admin/pages")
public class AdminPageController {
	
	@Autowired
	private PageRepository pgrp;
	
	
	
	@GetMapping()
	public String index(Model themodel) {
		
		List<Page> pages = pgrp.findByOrderBySortingAsc();
		
		themodel.addAttribute("pages", pages);
		
		return "admin/pages/index";
	}

	@GetMapping("/add")
	public String add(Model themodel) {
		
		themodel.addAttribute("page", new Page());
		
		return "admin/pages/add";
	}
	
	
	@PostMapping("/add")
	public String add(@Valid Page page,BindingResult bindingResult,RedirectAttributes redirectAttributes,Model themodel) {
		
		 if (bindingResult.hasErrors()) {
	            return "admin/pages/add";
	        }

	        redirectAttributes.addFlashAttribute("message", "Page added");
	        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

	        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-") : page.getSlug().toLowerCase().replace(" ", "-");

	        Page slugExists = pgrp.findBySlug(slug);

	        if ( slugExists != null ) {
	            redirectAttributes.addFlashAttribute("message", "Slug exists, choose another");
	            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
	            redirectAttributes.addFlashAttribute("page", page);

	        } else {
	            page.setSlug(slug);
	            page.setSorting(50);

	            pgrp.save(page);
	        }

	        return "redirect:/admin/pages/add";
	    }
		
	    @GetMapping("/edit/{id}")
		public String edit(@PathVariable int id,Model themodel) {
	    	
	    	Page page=pgrp.getOne(id);
	    	
	    	themodel.addAttribute("page", page);
	    	return "admin/pages/edit";
	    }
		
	    @PostMapping("/edit")
	    public String edit(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model themodel) {

	        Page pageCurrent = pgrp.getOne(page.getId());

	        if (bindingResult.hasErrors()) {
	           themodel.addAttribute("pageTitle", pageCurrent.getTitle());
	            return "admin/pages/edit";
	        }

	        redirectAttributes.addFlashAttribute("message", "Page edited");
	        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

	        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-") : page.getSlug().toLowerCase().replace(" ", "-");

	        Page slugExists = pgrp.findBySlugAndIdNot(slug, page.getId());

	        if ( slugExists != null ) {
	            redirectAttributes.addFlashAttribute("message", "Slug exists, choose another");
	            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
	            redirectAttributes.addFlashAttribute("page", page);

	        } else {
	            page.setSlug(slug);

	            pgrp.save(page);
	        }

	        return "redirect:/admin/pages/edit/" + page.getId();
	    }

	    @GetMapping("/delete/{id}")
	    public String edit(@PathVariable int id, RedirectAttributes redirectAttributes) {

	        pgrp.deleteById(id);

	        redirectAttributes.addFlashAttribute("message", "Page deleted");
	        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

	        return "redirect:/admin/pages";
	        
	    }
	    
	    
	    @PostMapping("/reorder")
	    public @ResponseBody String reorder(@RequestParam("id[]") int[] id) {
	        
	        int count = 1;
	        Page page;

	        for (int pageId : id) {
	            page = pgrp.getOne(pageId);
	            page.setSorting(count);
	            pgrp.save(page);
	            count++;
	        }

	        return "done";
	    }
	}

