package com.microservice2;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "MICROSERVICE-1")
public interface Client {
	
	@GetMapping("/message")   //same as FirstController code
	public String getData();  //same return type String in both places here and FirstController class
//when i call getData() method this will intern make a call to our microservices1 using this GetMapping
}
