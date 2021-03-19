package com.codemobiles.stock_backend.security;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.codemobiles.stock_backend.controller.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static com.codemobiles.stock_backend.security.SecurityConstants.*;

public class JWTAuthenticationFiltter extends UsernamePasswordAuthenticationFilter{



//	@Autowired
//	AuthenticationManager autenAuthenticationManager;
	
	private final AuthenticationManager authenticationManager;
	
	public JWTAuthenticationFiltter(AuthenticationManager authenticationManager) {
		
		this.authenticationManager = authenticationManager;
		this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/auth/login","POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			UserRequest userRequest = new ObjectMapper().readValue(request.getInputStream(), UserRequest.class);
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							userRequest.getUsername(),
							userRequest.getPassword(),
							new ArrayList<>()));
			
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		if(authResult.getPrincipal() != null) {
			org.springframework.security.core.userdetails.User user = 
					(org.springframework.security.core.userdetails.User) authResult.getPrincipal();
			
			String username = user.getUsername();
			if(username != null && username.length() >0) {
				Claims claims = Jwts.claims().setSubject(username).setIssuer("ship").setAudience("sss");
				List<String> roles = new ArrayList<>();
				user.getAuthorities().stream().forEach(
						authority -> roles.add(authority.getAuthority()));
				claims.put(CLAIM_ROLE, roles);
				claims.put("value", "ff");
				
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				
				Map<String, Object> responseJson =  new HashMap<>();
				responseJson.put("token", createToken(claims));
				
				OutputStream out = response.getOutputStream();
				ObjectMapper mapper = new ObjectMapper();
				mapper.writerWithDefaultPrettyPrinter().writeValue(out, responseJson);
				out.flush();
			}
		}else {
			
		}
	}
	
	
	private String createToken(Claims claims) {
		return Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	
	
	
}
