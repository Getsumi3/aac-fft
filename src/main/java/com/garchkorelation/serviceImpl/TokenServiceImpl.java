package com.garchkorelation.serviceImpl;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garchkorelation.model.Token;
import com.garchkorelation.model.User;
import com.garchkorelation.repository.TokenRepository;
import com.garchkorelation.service.TokenService;
import com.garchkorelation.service.UserService;

@Service("tokenService")
public class TokenServiceImpl implements TokenService {

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private UserService userService;

	@Override
	public Token createToken(String email) {
		Token token = new Token(email);
		tokenRepository.save(token);
		return token;
	}

	@Override
	public boolean isCorrect(String token) {
		try {
			return !findOneByToken(token).isExpired();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public User getUserByToken(Token token) {
		return userService.findByEmail(token.getEmail());
	}

	@Override
	public Token findOneByToken(String token) {
		return tokenRepository.findOneByToken(token);
	}

}
