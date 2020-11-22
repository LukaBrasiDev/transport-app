package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.lukabrasi.transportapp.auth.forms.LoginForm;
import pl.lukabrasi.transportapp.auth.services.UserService;

@Controller
public class HomeController {

    final UserService userService;

    @Autowired
    public HomeController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("loginForm", new LoginForm());
        return "index";
    }
}


