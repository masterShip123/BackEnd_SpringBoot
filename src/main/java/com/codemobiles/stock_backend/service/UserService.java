package com.codemobiles.stock_backend.service;

import com.codemobiles.stock_backend.controller.request.UserRequest;
import com.codemobiles.stock_backend.model.User;

public interface UserService {
	User register(UserRequest userRequest);
	
	User findUserByUsername(String username);
}
