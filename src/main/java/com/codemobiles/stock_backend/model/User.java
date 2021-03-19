package com.codemobiles.stock_backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "`user`")
@Accessors(chain = true)
public class User {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;
	
	@Column(nullable = false ,unique = true)
	private String username;
	
	@Column(nullable = false ,unique = true)
	private String password;
	
	@Column(nullable = false )
	private String role;
	
}
