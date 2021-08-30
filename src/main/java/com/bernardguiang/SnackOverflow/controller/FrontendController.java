package com.bernardguiang.SnackOverflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {
	
	
	// This will capture all GET calls to the website and point them to index.html (react client)
	@GetMapping({
		"/", 
		"/login",
		"/login/*",
		"/cart",
		"/cart/*",
		"/snacks*",
		"/snacks/*",
		"/contact/*",
		"/account",
		"/account/*",
		"/admin",
		"/admin/*",
		"/admin/**",
		"/checkout",
		"/checkout/*",
		"/error",
		"/error/**"
		})
    public String noroute(){

        return ("forward:/index.html");
    }

}