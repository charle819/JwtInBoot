package com.example.JwtInBoot.controller;

import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.JwtInBoot.dto.UserLoginDTO;
import com.example.JwtInBoot.dto.UserSaveDTO;
import com.example.JwtInBoot.entity.User;
import com.example.JwtInBoot.exception.UserAlreadyExistException;
import com.example.JwtInBoot.security.JwtTokenUtil;
import com.example.JwtInBoot.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userRequest){
		
		User user = userService.getUserByUserName(userRequest.getUserName());
		if(user == null) {throw new UsernameNotFoundException("No user found with username : "+userRequest.getUserName()); }
		
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userRequest.getUserName(),userRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final String token = jwtTokenUtil.generateToken(user);
		return new ResponseEntity<>(token, HttpStatus.OK);
	}
	
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public ResponseEntity<?> getAllUser(){
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
	}
	
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public ResponseEntity<?> saveUser(@Valid @RequestBody UserSaveDTO userRequest) {
		try {
			userService.saveUser(userRequest);
		} catch (UserAlreadyExistException e) {
			return new ResponseEntity<>("Failed to save new user. "+e.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("ERROR, Failed to save new user, check Logs", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
}
