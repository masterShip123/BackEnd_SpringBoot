package com.codemobiles.stock_backend.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.codemobiles.stock_backend.security.CustomUserDetailsService;
import com.codemobiles.stock_backend.security.JWTAuthenticationFiltter;
import com.codemobiles.stock_backend.security.JWTAuthentizationFiltter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private CustomUserDetailsService customUserDetailService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder; 
	
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailService).passwordEncoder(bCryptPasswordEncoder);
	}



	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
		.disable()
		.authorizeRequests()
		.antMatchers("/auth/resgister")
		.permitAll()
		.antMatchers(HttpMethod.DELETE,"/product/*").hasAnyAuthority("admin").anyRequest().authenticated()
		.and()
		.exceptionHandling()
		.authenticationEntryPoint((req,res,error) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
		.and()
		.addFilter(authenticationFilter())
		.sessionManagement()
		.and()
		.addFilter(new JWTAuthentizationFiltter(authenticationManager())).sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Bean
	UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception{
		final UsernamePasswordAuthenticationFilter  filter = new JWTAuthenticationFiltter(authenticationManager());
		filter.setAuthenticationManager(authenticationManager());
		return filter;
	}

}