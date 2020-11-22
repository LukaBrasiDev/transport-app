package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lukabrasi.transportapp.auth.services.UserService;
import pl.lukabrasi.transportapp.auth.services.UserSession;
import pl.lukabrasi.transportapp.form.UserForm;
import pl.lukabrasi.transportapp.service.OrderService;

@Controller
public class UserController {

    final OrderService orderService;
    final UserSession userSession;
    final UserService userService;

    @Autowired
    public UserController(UserSession userSession,OrderService orderService,UserService userService) {
        this.userSession = userSession;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/uzytkownicy")
    public String getFreighters(Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("logged", userSession.getUserEntity());
        return "user";
    }

    @PostMapping("/uzytkownicy")
    public String createFreighter(@ModelAttribute UserForm userForm, Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        orderService.saveUser(userForm);
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("logged", userSession.getUserEntity());
        return "user";
    }

    @GetMapping("/uzytkownik/{id}")
    public String getUserById(@PathVariable(value = "id") Long id, Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("users", orderService.getUserById(id));
        model.addAttribute("logged", userSession.getUserEntity());
        return "user";
    }

    @GetMapping("/edycjauzytkownik/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        if (!userSession.getUserEntity().getId().equals(id)) {
            return "redirect:/";
        }
        model.addAttribute("users", orderService.getUserById(id));
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("logged", userSession.getUserEntity());
        return "edituser";
    }

    @PostMapping("/edycjauzytkownik/{id}")
    public String updateUser(
            @PathVariable Long id,
            @ModelAttribute UserForm userForm) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
       userService.updateUser(id, userForm);
        return "redirect:/uzytkownicy";
    }


}
