package com.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByUsername(String username);
	User findByEmail(String email);
	boolean existsByUsername(String username); //existsBy checks the username and if exists gives boolean value
	boolean existsByEmail(String email); //existsBy checks the email and if exists gives boolean value
	
}
