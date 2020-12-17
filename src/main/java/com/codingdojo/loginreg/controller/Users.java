package com.codingdojo.loginreg.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codingdojo.loginreg.models.User;
import com.codingdojo.loginreg.service.UserService;

@Controller
	public class Users {
		private final UserService userService;
 
 public Users(UserService userService) {
     this.userService = userService;
 }
 
 	@RequestMapping("/registration")
 		public String registerForm(@ModelAttribute("user") User user) {
 			return "registration.jsp";
 }
 	@RequestMapping("/login")
 		public String login() {
 			return "login.jsp";
 }
 
 @PostMapping("/registration")
 public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
     if (result.hasErrors()) {
         return "registration.jsp";
     } else {
		User u = userService.registerUser(user);
		session.setAttribute("userId", u.getId());
		return "redirect:/home";
     }
	 
	 // if result has errors, return the registration page (don't worry about validations just now)
     // else, save the user in the database, save the user id in session, and redirect them to the /home route
 }
 
 @PostMapping("/login")
 public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session) {
     boolean isAuthenticated = userService.authenticateUser(email, password);
     if(isAuthenticated) {
    	 User u = userService.findByEmail(email);
    	 session.setAttribute("userId", u.getId());
    	 return "redirect:/home";
     } else {
    	 model.addAttribute("error", "Ivalid Credentials. Pleasse Try Again");
    	 return "login.jsp";
     }
	 
	 // if the user is authenticated, save their user id in session
     // else, add error messages and return the login page
 }
 
 @GetMapping("/home")
 public String home(HttpSession session, Model model) {
     Long userId = (Long) session.getAttribute("userId");
     User u = userService.findUserById(userId);
	 model.addAttribute("user", u);
	 return "homePage.jsp";
	 // get user from session, save them in the model and return the home page
 }
 @RequestMapping("/logout")
 public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/login";
	 // invalidate session
     // redirect to login page
 }
}
