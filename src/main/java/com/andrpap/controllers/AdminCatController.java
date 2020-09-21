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

import com.andrpap.models.CategoryRepository;
import com.andrpap.models.entities.Category;

@Controller
@RequestMapping("/admin/categories")
public class AdminCatController {
	
	@Autowired
	private CategoryRepository ctrp;
	
	@GetMapping
	public String index(Model themodel) {
		
		List<Category> categories = ctrp.findAllByOrderBySortingAsc();
		
		themodel.addAttribute("categories", categories);
		
		return ("admin/categories/index");
		
	}

	 @GetMapping("/add")
	    public String add(Model themodel) {
		 
		 themodel.addAttribute("category", new Category());
	        
	        return "admin/categories/add";
	    }

	    @PostMapping("/add")
	    public String add(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model themodel) {

	        if (bindingResult.hasErrors()) {
	            return "admin/categories/add";
	        }

	        redirectAttributes.addFlashAttribute("message", "Category added");
	        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

	        String slug = category.getName().toLowerCase().replace(" ", "-");

	        Category categoryExists = ctrp.findByName(category.getName());

	        if ( categoryExists != null ) {
	            redirectAttributes.addFlashAttribute("message", "Category exists, choose another");
	            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
	            redirectAttributes.addFlashAttribute("categoryInfo", category);

	        } else {
	            category.setSlug(slug);
	            category.setSorting(50);

	            ctrp.save(category);
	        }

	        return "redirect:/admin/categories/add";
	    }

	    @GetMapping("/edit/{id}")
	    public String edit(@PathVariable int id, Model model) {

	        Category category = ctrp.getOne(id);

	        model.addAttribute("category", category);

	        return "admin/categories/edit";
	        
	    }

	    @PostMapping("/edit")
	    public String edit(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model themodel) {

	        Category categoryCurrent = ctrp.getOne(category.getId());

	        if (bindingResult.hasErrors()) {
	           themodel.addAttribute("categoryName", categoryCurrent.getName());
	            return "admin/categories/edit";
	        }

	        redirectAttributes.addFlashAttribute("message", "Category edited");
	        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

	        String slug = category.getName().toLowerCase().replace(" ", "-");

	        Category categoryExists = ctrp.findByName(category.getName());

	        if ( categoryExists != null ) {
	            redirectAttributes.addFlashAttribute("message", "Category exists, choose another");
	            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

	        } else {
	            category.setSlug(slug);

	            ctrp.save(category);
	        }

	        return "redirect:/admin/categories/edit/" + category.getId();
	    }

	    @GetMapping("/delete/{id}")
	    public String edit(@PathVariable int id, RedirectAttributes redirectAttributes) {

	       ctrp.deleteById(id);

	        redirectAttributes.addFlashAttribute("message", "Category deleted");
	        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

	        return "redirect:/admin/categories";
	        
	    }

	    @PostMapping("/reorder")
	    public @ResponseBody String reorder(@RequestParam("id[]") int[] id) {
	        
	        int count = 1;
	        Category category;

	        for (int categoryId : id) {
	            category = ctrp.getOne(categoryId);
	            category.setSorting(count);
	            ctrp.save(category);
	            count++;
	        }

	        return "done";
	    }
}
