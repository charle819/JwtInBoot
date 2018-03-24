package com.example.JwtInBoot.exception;

import java.io.Serializable;

public class UserAlreadyExistException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	public UserAlreadyExistException() {
		super();
	}

	public UserAlreadyExistException(String exceptionMessage) {
		super(exceptionMessage);
	}
}
