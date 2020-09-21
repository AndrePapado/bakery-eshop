package com.andrpap;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.andrpap.models.CategoryRepository;
import com.andrpap.models.PageRepository;
import com.andrpap.models.entities.Cart;
import com.andrpap.models.entities.Category;
import com.andrpap.models.entities.Page;


// gia na einai to menu se oles tis selides //

@ControllerAdvice
public class CommonParts {

	 @Autowired
	    private PageRepository pgrp;

	    @Autowired
	    private CategoryRepository ctgrp;

	    @ModelAttribute
	    public void sharedData(Model themodel, HttpSession session, Principal principal) {

	        if (principal != null) {
	           themodel.addAttribute("principal", principal.getName());
	        }

	        List<Page> pages = pgrp.findByOrderBySortingAsc();

	        List<Category> categories = ctgrp.findAllByOrderBySortingAsc();
	        
	        // emfanisi cart se ka8e selida

	        boolean cartActive = false;

	        if (session.getAttribute("cart") != null) {

	            HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>)session.getAttribute("cart");

	            int size = 0;
	            double total = 0;

	            for (Cart value : cart.values()) {
	                size += value.getQuantity();
	                total += value.getQuantity() * Double.parseDouble(value.getPrice());
	            }

	            themodel.addAttribute("csize", size);
	            themodel.addAttribute("ctotal", total);

	            cartActive = true;
	        }
	        
	        

	        themodel.addAttribute("cpages", pages);
	        themodel.addAttribute("ccategories", categories);
	        themodel.addAttribute("cartActive", cartActive);


	    }

	    
}
