package com.authservice.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authservice.dto.APIResponse;
import com.authservice.dto.UserDto;
import com.authservice.entity.User;
import com.authservice.repository.UserRepository;

@Service
public class AuthService {
	 
	@Autowired
	 private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	//when i run this it gives error creating a bean, SpringIOC will not able to create a bean.
	//when there are third party libraries that we are using it spring IOC doesnot have information
	//which object to create so we have to tell our springIOC that whenever a PasswordEncoder is used
	//if this reference variable is used you have to create bean of the type BCryptPasswordEncoder.
	//for this go to config class AppSecurityConfig.java and add 
	//@Bean
	//public PasswordEncoder getEncodedPassword() {
	//	return new BCryptPasswordEncoder();
	//  }
	
	
	public APIResponse<String> register(UserDto userDto){
		
		APIResponse<String> response = new APIResponse<>();  //Creating API Response object
		
//Directly we cannot register user for this few things needs to perform:
		
	//Check whether username exists
		if(userRepository.existsByUsername(userDto.getUsername())) {
			response.setMessage("Registration Failed");
			response.setStatus(500);
			response.setData("User with user name already exists");
			return response;
		}
		
	//check whether Email exists
		if(userRepository.existsByEmail(userDto.getEmail())) {
			response.setMessage("Registration Failed");
			response.setStatus(500);
			response.setData("User with Email Id already exists");
			return response;
		}
		
	//Encode the password before saving that to the database
		//To encode password we need to generate new Encrypt Bean through Dependency Injection
		
		String encryptedPassword = passwordEncoder.encode(userDto.getPassword()); 
		//getting raw password using userDto.getPassword() and encode it.
		
		//Taking DTO content and copy that to user Entity for this Spring provides a class called
		//BeanUtils and use copyProperties() method as BeanUtils.copyProperties()
		User user=new User(); //Copying data of User entity to user
		BeanUtils.copyProperties(userDto, user);  //All entity content go to user but password now is raw password.
		
		user.setPassword(encryptedPassword); //Encrypting password
		
		user.setRole("ROLE_USER"); //Add this line to specify role of user
		
		User savedUser = userRepository.save(user);
		
		//If something went wrong throw an exception
		if(savedUser==null) {
			//Custom Exception
		}
		
		
	//finally save the user and return response as APIResponse	
		response.setMessage("Registration Completed");
		response.setStatus(201);
		response.setData("User has been registered");
		return response;
	
	}

}
