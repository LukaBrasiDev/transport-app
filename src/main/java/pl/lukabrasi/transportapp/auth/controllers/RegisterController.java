package pl.lukabrasi.transportapp.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lukabrasi.transportapp.auth.forms.RegisterForm;
import pl.lukabrasi.transportapp.auth.services.UserService;

@Controller
public class RegisterController {

    final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }


    @PostMapping("/register")
    public String register(@ModelAttribute RegisterForm registerForm,
                           Model model) {
        UserService.LoginResponse registerResponse = userService.registerUser(registerForm);
        if (registerResponse != UserService.LoginResponse.SUCCESS) {
            model.addAttribute("info", registerResponse);
            return "register";
        }

        return "redirect:/uzytkownicy";
    }


}