package com.example.JwtInBoot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class CommonController {

	@RequestMapping(value="status",method=RequestMethod.GET)
	public ResponseEntity<?> checkStatus() {
		return new ResponseEntity<>("Api is up and runnind", HttpStatus.OK);
	}
}
