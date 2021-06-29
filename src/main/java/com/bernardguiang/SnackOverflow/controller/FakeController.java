package com.bernardguiang.SnackOverflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FakeController {

	
	@GetMapping("/")
	public String entry() {
		return "hello";
	}
	
}
