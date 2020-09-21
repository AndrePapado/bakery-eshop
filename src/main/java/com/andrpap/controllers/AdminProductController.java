package com.andrpap.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.andrpap.models.CategoryRepository;
import com.andrpap.models.ProductRepository;
import com.andrpap.models.entities.Category;

import com.andrpap.models.entities.Product;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

	@Autowired
	private ProductRepository prdrp;

	@Autowired
	private CategoryRepository ctrp;

	@GetMapping
	public String index(Model themodel, @RequestParam(value = "page", required = false) Integer p) {

		int perPage = 5;
		int page = (p != null) ? p : 0;
		Pageable pageable = PageRequest.of(page, perPage);

		Page<Product> products = prdrp.findAll(pageable);
		List<Category> categories = ctrp.findAll();

		HashMap<Integer, String> cats = new HashMap<>();
		for (Category cat : categories) {
			cats.put(cat.getId(), cat.getName());
		}

		themodel.addAttribute("products", products);
		themodel.addAttribute("cats", cats);

		long count = prdrp.count();
		double pageCount = Math.ceil((double) count / (double) perPage);

		themodel.addAttribute("pageCount", (int) pageCount);
		themodel.addAttribute("perPage", perPage);
		themodel.addAttribute("count", count);
		themodel.addAttribute("page", page);

		return "admin/products/index";
	}

	@GetMapping("/add")
	public String add(Product product, Model themodel) {

		List<Category> categories = ctrp.findAll();
		themodel.addAttribute("categories", categories);

		return "admin/products/add";
	}

	@PostMapping("/add")
	public String add(@Valid Product product, BindingResult bindingResult, MultipartFile file,
			RedirectAttributes redirectAttributes, Model themodel) throws IOException {

		List<Category> categories = ctrp.findAll();

		if (bindingResult.hasErrors()) {
			themodel.addAttribute("categories", categories);
			return "admin/products/add"; 
		}

		boolean fileOK = false;
		byte[] bytes = file.getBytes();
		String filename = file.getOriginalFilename();
		Path path = Paths.get("src\\main\\resources\\gallery\\static" + filename);

		if (filename.endsWith("jpg") || filename.endsWith("png")) {
			fileOK = true;
		}

		redirectAttributes.addFlashAttribute("message", "Product added");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		String slug = product.getName().toLowerCase().replace(" ", "-");

		Product productExists = prdrp.findBySlug(slug);

		if (!fileOK) {
			redirectAttributes.addFlashAttribute("message", "Image must be a jpg or a png");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("product", product);
		} else if (productExists != null) {
			redirectAttributes.addFlashAttribute("message", "Product exists, choose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("product", product);
		} else {
			product.setSlug(slug);
			product.setImage(filename);
			prdrp.save(product);

			Files.write(path, bytes);
		}

		return "redirect:/admin/products/add";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Model themodel) {

		Product product = prdrp.getOne(id);
		List<Category> categories = ctrp.findAll();

		themodel.addAttribute("product", product);
		themodel.addAttribute("categories", categories);

		return "admin/products/edit";
	}

	@PostMapping("/edit")
	public String edit(@Valid Product product, BindingResult bindingResult, MultipartFile file,
			RedirectAttributes redirectAttributes, Model themodel) throws IOException {

		Product currentProduct = prdrp.getOne(product.getId());

		List<Category> categories = ctrp.findAll();

		if (bindingResult.hasErrors()) {
			themodel.addAttribute("productName", currentProduct.getName());
			themodel.addAttribute("categories", categories);
			return "admin/products/edit";
		}

		boolean fileOK = false;
		byte[] bytes = file.getBytes();
		String filename = file.getOriginalFilename();
		Path path = Paths.get("src/main/resources/static/images/" + filename);

		if (!file.isEmpty()) {
			if (filename.endsWith("jpg") || filename.endsWith("png")) {
				fileOK = true;
			}
		} else {
			fileOK = true;
		}

		redirectAttributes.addFlashAttribute("message", "Product edited");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		String slug = product.getName().toLowerCase().replace(" ", "-");

		Product productExists = prdrp.findBySlugAndIdNot(slug, product.getId());

		if (!fileOK) {
			redirectAttributes.addFlashAttribute("message", "Image must be a jpg or a png");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("product", product);
		} else if (productExists != null) {
			redirectAttributes.addFlashAttribute("message", "Product exists, choose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("product", product);
		} else {

			product.setSlug(slug);

			if (!file.isEmpty()) {
				Path path2 = Paths.get("src/main/resources/static/images/" + currentProduct.getImage());
				Files.delete(path2);
				product.setImage(filename);
				Files.write(path, bytes);
			} else {
				product.setImage(currentProduct.getImage());
			}

			prdrp.save(product);

		}

		return "redirect:/admin/products/edit/" + product.getId();
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) throws IOException {

		Product product = prdrp.getOne(id);
		Product currentProduct = prdrp.getOne(product.getId());

		Path path2 = Paths.get("src/main/resources/static/images/" + currentProduct.getImage());
		Files.delete(path2);
		prdrp.deleteById(id);

		redirectAttributes.addFlashAttribute("message", "Product deleted");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		return "redirect:/admin/products";

	}

}
