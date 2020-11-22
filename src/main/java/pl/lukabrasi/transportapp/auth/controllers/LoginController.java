package pl.lukabrasi.transportapp.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lukabrasi.transportapp.auth.forms.LoginForm;
import pl.lukabrasi.transportapp.auth.services.UserService;

@Controller
public class LoginController {


    final UserService userService;

    @Autowired
    public LoginController(UserService userService) {

        this.userService = userService;
    }




    @PostMapping("/")
    public String login(@ModelAttribute LoginForm loginForm,
                        Model model) { //zmienna1,  zmienna2, zmienna3
        UserService.LoginResponse loginResponse = userService.login(loginForm);

        if (loginResponse != UserService.LoginResponse.SUCCESS) {
            model.addAttribute("info", loginResponse);
            return "index";
        }

        return "redirect:/zlecenia";
    }



    @GetMapping("/logout")
    public String logout() {

        userService.logout();
        return "redirect:/";
    }


}
