package com.garchkorelation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.garchkorelation.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long>{

	Token findOneByToken(String token);
	
}
