package com.garchkorelation.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.garchkorelation.service.UserService;

public class UserRESTController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/email/{hmac}/confirm" }, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@PreAuthorize("hasRole('ROLE_USER')")
	public void confirmEmail(@PathVariable("hmac") String hmac) {
		userService.verifyEmail(hmac);
	}
}
