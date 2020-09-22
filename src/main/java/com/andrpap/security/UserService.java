package com.andrpap.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.andrpap.models.AdminRepository;
import com.andrpap.models.UserRepository;
import com.andrpap.models.entities.Admin;
import com.andrpap.models.entities.User;

public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userepository;
	
	@Autowired
	private AdminRepository  adminrepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		User user = userepository.findByUsername(username);
		
	
		Admin admin = adminrepository.findByUsername(username);
		
		if (user!= null) {
			return user;
		}
		
		
		if (admin!=null) {
			
			return admin;
		}
		
		throw new UsernameNotFoundException("User" + username + " does not exist");
	}

}
