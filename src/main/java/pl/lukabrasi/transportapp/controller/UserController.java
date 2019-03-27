package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lukabrasi.transportapp.form.UserForm;
import pl.lukabrasi.transportapp.service.OrderService;

@Controller
public class UserController {

    final OrderService orderService;

    @Autowired
    public UserController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/user")
    public String getFreighters(Model model) {

        model.addAttribute("users", orderService.getUsers());

        return "user";
    }

    @PostMapping("/user")
    public String createFreighter(@ModelAttribute UserForm userForm, Model model) {
        orderService.saveUser(userForm);
        model.addAttribute("users", orderService.getUsers());
        return "user";
    }


    @GetMapping("/user/{id}")
    public String getUserById(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("users", orderService.getUserById(id));
        return "user";
    }

    @GetMapping("/edituser/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {
        model.addAttribute("users", orderService.getUserById(id));
        model.addAttribute("userForm", new UserForm());
        return "edituser";
    }


    @PostMapping("/edituser/{id}")
    public String updateUser(
            @PathVariable Long id,
            @ModelAttribute UserForm userForm) {

        orderService.updateUser(id, userForm);

        return "redirect:/user";

    }



}
