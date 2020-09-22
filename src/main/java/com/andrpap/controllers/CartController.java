package com.andrpap.controllers;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.andrpap.models.ProductRepository;
import com.andrpap.models.entities.Cart;
import com.andrpap.models.entities.Product;

@Controller
@RequestMapping("/cart")
@SuppressWarnings("unchecked")
public class CartController {

	@Autowired
	private ProductRepository prdrp;

	@GetMapping("/add/{id}")
	public String add(@PathVariable int id, HttpSession session, Model themodel) {

		Product product = prdrp.getOne(id);

		if (session.getAttribute("cart") == null) {

			HashMap<Integer, Cart> cart = new HashMap<>();

			cart.put(id, new Cart(id, product.getName(), 1, product.getPrice(), product.getImage()));

			session.setAttribute("cart", cart);

		} else {

			HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");

			if (cart.containsKey(id)) {
				int qnt = cart.get(id).getQuantity();
				cart.put(id, new Cart(id, product.getName(), ++qnt, product.getPrice(), product.getImage()));

			} else {
				cart.put(id, new Cart(id, product.getName(), 1, product.getPrice(), product.getImage()));
				session.setAttribute("cart", cart);
			}
		}

		HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");

        int size = 0;
        double total = 0;

        for (Cart value : cart.values()) {
            size += value.getQuantity();
            total += value.getQuantity() * Double.parseDouble(value.getPrice());
        }

        themodel.addAttribute("size", size);
        themodel.addAttribute("total", total);

       
        return "cartview";
		

	}
    @GetMapping("/subtract/{id}")
    public String subtract(@PathVariable int id, HttpSession session, Model model, HttpServletRequest httpServletRequest) {

        Product product = prdrp.getOne(id);

        HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");

        int qnt = cart.get(id).getQuantity();
        if (qnt == 1) {
            cart.remove(id);
            if (cart.size() == 0) {
                session.removeAttribute("cart");
            } 
        } else {
            cart.put(id, new Cart(id, product.getName(),--qnt, product.getPrice(), product.getImage()));
        }

        String refererLink = httpServletRequest.getHeader("referer");

        return "redirect:" + refererLink;
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable int id, HttpSession session, Model themodel, HttpServletRequest httpServletRequest) {

        HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");

        cart.remove(id);
        if (cart.size() == 0) {
            session.removeAttribute("cart");
        } 

        String refererLink = httpServletRequest.getHeader("referer");

        return "redirect:" + refererLink;
    }

    @GetMapping("/clear")
    public String clear(HttpSession session, HttpServletRequest httpServletRequest) {

        session.removeAttribute("cart");

        String refererLink = httpServletRequest.getHeader("referer");

        return "redirect:" + refererLink;
    }

    @RequestMapping("/view")
    public String view(HttpSession session, Model themodel) {

        if (session.getAttribute("cart") == null) {
            return "redirect:/";
        }

        HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
        themodel.addAttribute("cart", cart);
        themodel.addAttribute("notCartViewPage", true);

        return "cart";
    }
    
   
}
