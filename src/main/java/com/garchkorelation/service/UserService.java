package com.garchkorelation.service;

import java.util.List;

import com.garchkorelation.model.User;

public interface UserService {

	void save(User user);

	void update(User user);
	
	void updatePassword(User user);

	User findByUsername(String username);

	User findByEmail(String email);
	
	boolean isEmailRegistered(String email);
	
	User findById(Long id);

	User getCurrentUser();
	
	boolean isLogedIn();

	List<User> getAllUsers();

	void delete(Long id);

	void setModeratorRole(Long id);

	void removeModeratorRole(Long id);
	
	void removeUnverifiedRole(Long id);
	
	List<User> getUserListForNews();
	
	void verifyEmail(String hmac);
	
	boolean sendForgotenUsername(String email);
	
	boolean sendForgotenPassword(String email);
	
}
