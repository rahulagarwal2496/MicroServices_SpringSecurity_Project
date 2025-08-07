package com.authservice.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.authservice.service.CustomUserDetailsService;
import com.authservice.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	@Autowired	
	private JwtService jwtService;
	
	@Autowired
	private CustomUserDetailsService UserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization"); //In this method getHeader put your key name which is going to give Token
		//System.out.println(authHeader);
		
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String jwt = authHeader.substring(7); //substring will skip the first seven characters i.e Bearer and one space
			//System.out.println(jwt); //it prints only Token
			
			String username = jwtService.validateTokenAndRetrieveSubject(jwt); //whatever Token i extracted in jwt variable i am giving to 
			//public String validateTokenAndRetrieveSubject(String token) { } of JwtService class.
			//System.out.println(username);
			
			 if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				 var userDetails = UserDetailsService.loadUserByUsername(username);
				 
				 var authToken = new UsernamePasswordAuthenticationToken(
	                        userDetails, null, userDetails.getAuthorities()); //using java 10 features here
				 
				 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				//i am further taking Token and this token has username and role and to the same object i am setting up URL
	                SecurityContextHolder.getContext().setAuthentication(authToken);
	                //sets the token into a ContextHolder this is like taking permission from spring security. 
			 }
		}
		filterChain.doFilter(request, response); //this line is must 
	}

}
