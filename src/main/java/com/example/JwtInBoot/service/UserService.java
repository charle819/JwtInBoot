package com.example.JwtInBoot.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.JwtInBoot.dto.UserSaveDTO;
import com.example.JwtInBoot.entity.User;
import com.example.JwtInBoot.exception.UserAlreadyExistException;

@Service
public class UserService implements UserDetailsService{

	
	private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	private static List<User> users = new ArrayList<>();
	
	public UserService() {
		users = generateUsers();
	}
	
	private List<User> generateUsers() {
		List<User> userList = new ArrayList<>();
		userList.add(new User(1, "mike", "$2a$10$j33c0AFkDXM6acnZQvqza.i8bPnDkE8LOFrCDDUVuE3O09GwnEYpq", "mike@company.com"));
		userList.add(new User(2, "luke", "$2a$10$1REY2zVbBKcnKObtBJThqus1Ospq1MBqq0vUlNV0z4HuI2.4HjUwq", "luke@company.com"));
		return userList;
	}
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = getUserByUserName(userName);
		if(user == null) { throw new UsernameNotFoundException("No user found with username : "+userName);}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),getAuthorities(Arrays.asList("ROLE_ADMIN","ROLE_USER")));
	}
	
	private List<SimpleGrantedAuthority> getAuthorities(List<String> roles){
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		roles.stream().forEach(role->authorities.add(new SimpleGrantedAuthority(role)));
		return authorities;
	}

	public User getUserByUserName(String userName) {
		return users.stream().filter(u -> userName.equalsIgnoreCase(u.getUserName())).findAny().orElse(null);
	}

	public User getUserByEmail(String email) {
		return users.stream().filter(u -> email.equalsIgnoreCase(u.getEmail())).findAny().orElse(null);
	}

	public List<User> getAllUsers() { // For test remove latter
		return users;
	}

	private boolean doesUserExistyUserName(String userName) {
		return (getUserByUserName(userName) != null) ? true : false;
	}
	
	private boolean doesUserExistByEmail(String email) {
		return (getUserByEmail(email) !=null)?true:false;
	}
	
	public boolean saveUser(UserSaveDTO user) throws UserAlreadyExistException {
		if( doesUserExistByEmail(user.getEmail()) || doesUserExistyUserName(user.getUserName())) { throw new UserAlreadyExistException("User with requested userName or email already exist");}
		return users.add(new User(users.size()+1,user.getUserName(),passwordEncoder.encode(user.getPassword()),user.getEmail()));
	}
}
