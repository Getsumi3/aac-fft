package com.garchkorelation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.garchkorelation.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	User findByEmail(String email);

	Long getIdByUsername(String username);
	
	List<User> findByReceiveNewsletterTrue();

}
