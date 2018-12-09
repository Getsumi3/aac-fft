package com.garchkorelation.model;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.garchkorelation.util.TimeUtil;

@Entity(name = "token")
public class Token {

	private Long id;
	private String email;
	private String token;
	private String expirationDate;

	public Token() {
	}

	public Token(String email) {
		this.email = email;
		this.token = UUID.randomUUID().toString();
		this.expirationDate = TimeUtil.getExpirationDateForToken();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Transient
	public boolean isExpired() throws ParseException {
		return TimeUtil.getDatefromString(this.expirationDate).before(new Date());
	}

}
