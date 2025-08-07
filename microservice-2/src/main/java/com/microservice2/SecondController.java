package com.microservice2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecondController {
	
	@Autowired
	private Client client; //this is interface for which we are doing dependency injection.
	
	@GetMapping("/fromsecondcontroller")
	public String getMessageFromMicroservices1() {
		return client.getData(); //it calls getData
	}

}
