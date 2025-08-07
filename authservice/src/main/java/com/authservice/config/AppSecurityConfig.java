package com.authservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.authservice.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	private String[] openUrl= {
			"/api/v1/auth/register",
			"/api/v1/auth/login",
    		"/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
       };  	//Created an String Array to keep Urls open
	
	@Bean
	public PasswordEncoder getEncodedPassword() {
		return new BCryptPasswordEncoder();
	} //it creates an object of BCryptPasswordEncoder and give it to Spring IOC.

	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public AuthenticationProvider authProvider() {

		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		
		authProvider.setUserDetailsService(customUserDetailsService);
		//now DaoAuthenticationProvider is able to supply the username to it
		authProvider.setPasswordEncoder(getEncodedPassword()); //since password is encoded so decrypting password
		return authProvider;
	}
	
	//In this we need to Configure how to keep
	//1. which url open 
	//2. which url to authenticate
	//3. which URL To do Authorization
	@Bean 	//it will create an object of http and give it to IOC
	public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception{
		//whatever input coming from this http.authorizeHttpRequests simply goto req
		//http.authorizeHttpRequests(
		//		req->{		//we are using lambdas expression here req-> 
		//in latest version of springframework we have to use like this otherwise it will not work
		//			req.anyRequest().permitAll(); //allows all urls coming to req
		//		}
		//		);
		//return http.build();  //to finish object and apply return on it
		
		//the incoming url if is this "http://localhost:8080/api/v1/auth/welcome" permit all 
		//any request apart from that authenticated.
		//http.csrf(csrf -> csrf.disable())  //Disable CSRF

	http.csrf(csrf -> csrf.disable())  //Disable CSRF
	.authorizeHttpRequests(
			req->
				req.requestMatchers(openUrl)
				.permitAll()
				//.requestMatchers("/api/v1/welcome/message").hasRole("USER") //only as USER you can access this
				//to access as multiple role use hasAnyRole() method and supply all roles names into it.
				//.requestMatchers("/api/v1/welcome/message").hasAnyRole("USER","ADMIN")
				.requestMatchers("/api/v1/welcome/message").hasAnyRole("USER","ADMIN")
				.anyRequest()
				.authenticated()
			)  //httpBasic() for doing form related testing.
	.authenticationProvider(authProvider())
    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); 
	//this tells run jwtFilter before running internal filters.
	
	return http.build();
	}
}
