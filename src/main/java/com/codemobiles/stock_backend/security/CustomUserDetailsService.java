package com.codemobiles.stock_backend.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codemobiles.stock_backend.model.User;
import com.codemobiles.stock_backend.service.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user =  userService.findUserByUsername(username);
		if(user != null) {
			Set<GrantedAuthority> roles = new HashSet<>();
			roles.add(new SimpleGrantedAuthority(user.getRole()));
			
			List<GrantedAuthority> authority = new ArrayList<GrantedAuthority>(roles);
			return new org.springframework.security.core
					.userdetails.User(user.getUsername(),user.getPassword(),authority);
		}else {
			throw new UsernameNotFoundException("Username : "+ username + " does not exist. ");
		}
	}

}
