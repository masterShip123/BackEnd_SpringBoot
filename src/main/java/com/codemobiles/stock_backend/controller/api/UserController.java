package com.codemobiles.stock_backend.controller.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codemobiles.stock_backend.controller.request.UserRequest;
import com.codemobiles.stock_backend.exception.ValidationException;
import com.codemobiles.stock_backend.model.User;
import com.codemobiles.stock_backend.service.UserService;

@RequestMapping("/auth")
@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/resgister")
	public User register(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			bindingResult.getFieldErrors().stream().forEach(fieldError -> {
				throw new ValidationException(fieldError.getField() + ": " + fieldError.getDefaultMessage());
			});
		}
		
		return userService.register(userRequest);
	}
}
