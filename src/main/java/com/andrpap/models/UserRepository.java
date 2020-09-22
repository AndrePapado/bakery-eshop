package com.andrpap.models;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andrpap.models.entities.User;

public interface UserRepository extends JpaRepository<User,Integer>{
	
	User findByUsername(String username);

}
