package com.codemobiles.stock_backend.exception;

public class UserDuplicateException extends RuntimeException{
	
	public UserDuplicateException(String username) {
		super("UserName : "+ username+ " already exists. ");
	}

}
