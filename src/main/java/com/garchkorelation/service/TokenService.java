package com.garchkorelation.service;

import java.text.ParseException;

import com.garchkorelation.model.Token;
import com.garchkorelation.model.User;

public interface TokenService {

	Token createToken(String email);

	boolean isCorrect(String token) throws ParseException;

	User getUserByToken(Token token);
	
	Token findOneByToken(String token);
	
}
