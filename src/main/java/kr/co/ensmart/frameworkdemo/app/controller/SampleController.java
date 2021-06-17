package kr.co.ensmart.frameworkdemo.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample")
public class SampleController {
	@GetMapping("/main")
	String viewMain() {
		return "sample/main";
	}
	
}
