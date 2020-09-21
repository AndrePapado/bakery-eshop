package com.andrpap.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.andrpap.models.CategoryRepository;
import com.andrpap.models.ProductRepository;
import com.andrpap.models.entities.Category;

import com.andrpap.models.entities.Product;


@Controller
@RequestMapping("/categor")
public class CategoriesContoller {

	@Autowired
	private CategoryRepository ctgr;
	
	@Autowired
	private ProductRepository prdrp;
	
	
	
	@GetMapping("/{slug}")
    public String category(@PathVariable String slug, Model themodel, @RequestParam(value="page", required = false) Integer p) {

        int perPage = 6;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);
        long count = 0;

        if (slug.equals("all")) {

            Page <Product> products = prdrp.findAll(pageable);

            count = prdrp.count();

            themodel.addAttribute("products", products);
        } else {

            Category category = ctgr.findBySlug(slug);

            if (category == null) {
                return "redirect:/";
            }

            int categoryId = category.getId();
            String categoryName = category.getName();
            List<Product> products = prdrp.findAllByCategoryId(Integer.toString(categoryId), pageable);

            count = prdrp.countByCategoryId(Integer.toString(categoryId));

          themodel.addAttribute("products", products);
            themodel.addAttribute("categoryName", categoryName);
        }

        double pageCount = Math.ceil((double)count / (double)perPage);

        themodel.addAttribute("pageCount", (int)pageCount);
        themodel.addAttribute("perPage", perPage);
        themodel.addAttribute("count", count);
        themodel.addAttribute("page", page);

        return "products";
    }

}
