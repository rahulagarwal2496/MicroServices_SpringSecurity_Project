package com.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authservice.dto.APIResponse;
import com.authservice.dto.LoginDto;
import com.authservice.dto.UserDto;
import com.authservice.service.AuthService;
import com.authservice.service.JwtService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	@Autowired  //it gives error creating a bean so add this AuthenticationManager in config file.
	private AuthenticationManager authManager;
	
	@Autowired
	private AuthService authService; //injecting bean and use this authService below coding part
	
	@Autowired
	private JwtService jwtService;  //code for generation of JWT Token
	
	@PostMapping("/register")
	public ResponseEntity<APIResponse<String>> register(@RequestBody UserDto dto){
		APIResponse<String> response = authService.register(dto);
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
	}
	
	@PostMapping("/login")
	public ResponseEntity<APIResponse<String>> login(@RequestBody LoginDto dto){ //this dto has
														//username and password that user entered
		APIResponse<String> response = new APIResponse<>(); 
//You can't directly give username and password to AuthenticationManager for this apply this line
		UsernamePasswordAuthenticationToken token = 
				new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword());
		
		try {
			 Authentication authenticate = authManager.authenticate(token);
			 
			 if(authenticate.isAuthenticated()) {
				 //code for Generating JWT Token
				 String jwtToken = jwtService.generateToken(dto.getUsername(),
			                authenticate.getAuthorities().iterator().next().getAuthority());

			            response.setMessage("Login Successful");
			            response.setStatus(200);
			            response.setData(jwtToken);  // return JWT
			            return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
			        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 response.setMessage("Failed");
		 response.setStatus(401);
		 response.setData("Un-Authorized Access");
		 return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
	 }
		
}

