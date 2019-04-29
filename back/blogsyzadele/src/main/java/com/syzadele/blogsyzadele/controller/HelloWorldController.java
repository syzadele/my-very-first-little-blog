package com.syzadele.blogsyzadele.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/HelloWorldController")
public class HelloWorldController {

	private String word = "hello ";
	@RequestMapping("/Print")
	public String print(@RequestParam(value="name", defaultValue = "World") String name) {
		return word + name;
	}
	
	
	
}
