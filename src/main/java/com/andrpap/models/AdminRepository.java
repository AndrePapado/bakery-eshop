package com.andrpap.models;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andrpap.models.entities.Admin;

public interface AdminRepository extends JpaRepository<Admin,Integer> {

	Admin findByUsername(String username);
	

}
