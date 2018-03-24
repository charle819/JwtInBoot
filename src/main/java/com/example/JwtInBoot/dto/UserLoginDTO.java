package com.example.JwtInBoot.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserLoginDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@JsonProperty("username")
	private String userName;

	@NotBlank
	private String password;

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

	public UserLoginDTO() {
	}

	public UserLoginDTO(String userName, @NotBlank String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

}
