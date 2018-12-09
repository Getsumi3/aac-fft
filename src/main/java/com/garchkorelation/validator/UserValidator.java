package com.garchkorelation.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.garchkorelation.model.User;
import com.garchkorelation.service.UserService;

@Component
public class UserValidator implements Validator {

	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		User user = (User) o;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
		if (user.getUsername().length() < 3 || user.getUsername().length() > 32) {
			errors.rejectValue("username", "Size.userForm.username");
		}
		if (userService.findByUsername(user.getUsername()) != null) {
			errors.rejectValue("username", "Duplicate.userForm.username");
		}
		if (containsSpecialChars(user.getUsername())) {
			errors.reject("username", "Special.userForm.username");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
		if (userService.findByEmail(user.getEmail()) != null) {
			errors.rejectValue("email", "Duplicate.userForm.email");
		}
		if (!isValidEmailAddress(user.getEmail())) {
			errors.rejectValue("email", "Valid.userForm.email");
		}

		if (!user.getEmailConfirm().equals(user.getEmail())) {
			errors.rejectValue("emailConfirm", "Diff.userForm.emailConfirm");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
		if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
			errors.rejectValue("password", "Size.userForm.password");
		}

		if (!isValid("^(.*\\d).*$", user.getPassword())) {
			errors.rejectValue("password", "Number.userForm.password");
		}

		if (!isValid("^(.*[a-z]).*$", user.getPassword())) {
			errors.rejectValue("password", "Lowercase.userForm.password");
		}

		if (!isValid("^(.*?[A-Z]).*$", user.getPassword())) {
			errors.rejectValue("password", "Uppercase.userForm.password");
		}

//		if (!isValid("^(.*[@#$%]).*$", user.getPassword())) {
//			errors.rejectValue("password", "Special.userForm.password");
//		}

		if (!user.getPasswordConfirm().equals(user.getPassword())) {
			errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "country", "NotEmpty");

	}

	private boolean isValid(String pattern, String password) {
		return Pattern.compile(pattern).matcher(password).matches();

	}

	private boolean containsSpecialChars(String string) {
		Matcher m = Pattern.compile("\\W").matcher(string);
		return m.find();
	}

	public boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		Pattern p = Pattern.compile(ePattern);
		Matcher m = p.matcher(email);
		return m.matches();
	}

}
