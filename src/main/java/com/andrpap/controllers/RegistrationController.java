package com.andrpap.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.andrpap.models.UserRepository;
import com.andrpap.models.entities.User;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private UserRepository userepository;

	@Autowired
	private PasswordEncoder passenconder;
	
	@GetMapping
	public String register(User user) {
		 return ("register");
	}

	@PostMapping
	public String register(@Valid User user,BindingResult bindingResult,Model themodel) {
		
		if(bindingResult.hasErrors()) {
			
			 return ("register");
		}
		
		user.setPassword(passenconder.encode(user.getPassword()));
        userepository.save(user);

        return "redirect:/login";
	}
}
