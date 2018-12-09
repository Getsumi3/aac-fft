package com.garchkorelation.web;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.garchkorelation.bean.PasswordRecoveryBean;
import com.garchkorelation.model.User;
import com.garchkorelation.service.EmailSenderService;
import com.garchkorelation.service.SecurityService;
import com.garchkorelation.service.StockService;
import com.garchkorelation.service.TokenService;
import com.garchkorelation.service.UserService;
import com.garchkorelation.validator.UserValidator;

@Controller
@RequestMapping("/")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private SecurityService securityService;
	
	@Autowired 
	private EmailSenderService emailSenderService;

	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private StockService stockService;
	
	// Registration
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public String registration(Model model) {
		model.addAttribute("userForm", new User());
		return "registration";
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
		userValidator.validate(userForm, bindingResult);

		if (bindingResult.hasErrors()) {
			return "registration";
		}

		userService.save(userForm);

		securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());
		
//		emailSenderService.sendVerification(); 

		return "redirect:/home";
	}

	// Login
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, String error, String logout) {
		if (error != null)
			model.addAttribute("error", "Your username and password are invalid.");

		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");

		return "login";
	}

	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public String home(Model model) {
		return "home";
	}

	// Send login/pass
	@RequestMapping(value = "/forgotUsername", method = RequestMethod.GET)
	public String forgotUsername(Model model) {
		model.addAttribute("forgotTypeText", "Get username!");
		model.addAttribute("forgotType", "Username");
		return "forgot";
	}

	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	public String forgotPassword(Model model) {
		model.addAttribute("forgotTypeText", "Get new password!");
		model.addAttribute("forgotType", "Password");
		return "forgot";
	}

	@RequestMapping(value = "/forgotUsername/get", method = RequestMethod.GET)
	public String forgotUsername(Model model, @RequestParam("email") String email) {
		forgotVariables("Get username!", "Username", model, userService.sendForgotenUsername(email));
		return "forgot";
	}

	@RequestMapping(value = "/forgotPassword/get", method = RequestMethod.GET)
	public String forgotPassword(Model model, @RequestParam("email") String email) {
		forgotVariables("Get new password!", "Password", model, userService.sendForgotenPassword(email));
		return "forgot";
	}

	private void forgotVariables(String typeText, String type, Model model, boolean sendSuccess) {
		model.addAttribute("forgotTypeText", typeText);
		model.addAttribute("forgotType", type);
		if (sendSuccess) {
			model.addAttribute("sent", "Email sent!");
		} else {
			model.addAttribute("sent", "Email doesn't registered!");
		}
	}

	// Renew pass
	@RequestMapping(value = "/renewPassword/{token}", method = RequestMethod.GET)
	public String renewPassword(Model model, @PathVariable("token") String token) {
		model.addAttribute("passwordForm", new PasswordRecoveryBean());
		model.addAttribute("token", new PasswordRecoveryBean());
		try {
			if (tokenService.isCorrect(token)) {
				return "renew";
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "errors/tokenExpired";

	}

	// TODO: validation userForm with only password
	@RequestMapping(value = "/renewPassword/{token}", method = RequestMethod.POST)
	public String renewPassword(@PathVariable("token") String token, @ModelAttribute("passwordForm") PasswordRecoveryBean passwordForm, BindingResult bindingResult, Model model) {
		// userValidator.validate(userForm, bindingResult);

		User user = userService.findByEmail(tokenService.findOneByToken(token).getEmail());
		
		user.setPassword(passwordForm.getPassword());
		user.setPasswordConfirm(passwordForm.getPassword());
		userService.updatePassword(user);
		
		securityService.autologin(user.getUsername(), user.getPasswordConfirm());

		return "redirect:/home";
	}
	
	@RequestMapping(value = "/header", method = RequestMethod.GET)
	public String header() {
		return "header";
	}
	
	@RequestMapping(value = "/footer", method = RequestMethod.GET)
	public String footer() {
		return "footer";
	}
	
	@RequestMapping(value = "/idea", method = RequestMethod.GET)
	public String openIdea(Model model) {
		return "idea";
	}
	

}
