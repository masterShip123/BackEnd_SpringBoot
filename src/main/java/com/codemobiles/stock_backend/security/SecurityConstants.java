package com.codemobiles.stock_backend.security;

public interface SecurityConstants {
	String SECRET_KEY = "shipTOKEN";
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_AUTHORIZATION = "Authorization";
	String CLAIM_ROLE = "roles";
	long EXPIRATION_TIME = (5* 60 * 1000);

}
