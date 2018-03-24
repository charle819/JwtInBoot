package com.example.JwtInBoot.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserSaveDTO implements Serializable {

	@JsonProperty("username")
	private String userName;

	@NotBlank
	private String password;

	@NotBlank
	private String email;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserSaveDTO() {
		// TODO Auto-generated constructor stub
	}

	public UserSaveDTO(@NotBlank String userName, @NotBlank String password, @NotBlank String email) {
		super();
		this.userName = userName;
		this.password = password;
		this.email = email;
	}

}
