package com.garchkorelation.serviceImpl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.garchkorelation.model.Role;
import com.garchkorelation.model.User;
import com.garchkorelation.repository.UserRepository;
import com.garchkorelation.service.EmailSenderService;
import com.garchkorelation.service.RoleService;
import com.garchkorelation.service.TokenService;
import com.garchkorelation.service.UserService;
import com.garchkorelation.util.HmacSha1Signature;
import com.garchkorelation.util.StaticVar;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	private TokenService tokenService;

	@Override
	public void save(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		HashSet<Role> set = new HashSet<>();
		set.add(roleService.findUserRole());
		set.add(roleService.findUnverifiedRole());
		user.setRoles(set);
		userRepository.save(user);
	}

	@Override
	public void update(User user) {
		userRepository.save(user);
	}

	@Override
	public void updatePassword(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User getCurrentUser() {
		try {
			return findByUsername(((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
		} catch (ClassCastException ex) {
			return null;
		}
	}
	
	@Override
	public boolean isLogedIn() {
		return getCurrentUser()!=null;
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public void delete(Long id) {
		userRepository.delete(id);
	}

	@Override
	public void setModeratorRole(Long id) {
		User user = userRepository.findOne(id);
		HashSet<Role> set = new HashSet<Role>(user.getRoles());
		set.add(roleService.findModeratorRole());
		user.setRoles(set);
		update(user);
	}

	@Override
	public void removeModeratorRole(Long id) {
		removeRole(id, StaticVar.MODERATOR_ID);
	}

	@Override
	public void removeUnverifiedRole(Long id) {
		removeRole(id, StaticVar.UNVERIFIED_ID);
	}

	private void removeRole(Long userId, Long roleId) {
		User user = userRepository.findOne(userId);
		HashSet<Role> set = new HashSet<Role>(user.getRoles());
		for (Iterator<Role> iterator = set.iterator(); iterator.hasNext();) {
			Role role = iterator.next();
			if (role.getId() == roleId)
				iterator.remove();

		}
		user.setRoles(set);
		update(user);
	}

	@Override
	public User findById(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public List<User> getUserListForNews() {
		return userRepository.findByReceiveNewsletterTrue();
	}

	@Override
	public boolean isEmailRegistered(String email) {
		return findByEmail(email) != null ? true : false;
	}

	@Override
	public void verifyEmail(String hmac) {
		User currentUser = getCurrentUser();
		try {
			String rightHmac = HmacSha1Signature.calculateRFC2104HMAC(HmacSha1Signature.getValueFromUser(currentUser));
			if (rightHmac.equals(hmac)) {
				removeUnverifiedRole(currentUser.getId());
			} else {
				throw new InvalidKeyException();
			}
		} catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean sendForgotenUsername(String email) {
		if (isEmailRegistered(email)) {
			User user = findByEmail(email);
			emailSenderService.sendEmail(email, "forgot username", user.getUsername());
			return true;
		}
		return false;
	}

//	TODO: style for emails
	@Override
	public boolean sendForgotenPassword(String email) {
		if (isEmailRegistered(email)) {
			String token = tokenService.createToken(email).getToken();
			String mail = 
					"<br>"+StaticVar.CONTEXT_PATH + "/renewPassword/" + token +
						  "<br>"+StaticVar.CONTEXT_PATH_REMOTE + "/renewPassword/" + token;

			emailSenderService.sendEmail(email, "renew password", mail);
			return true;
		}
		return false;

	}

}
