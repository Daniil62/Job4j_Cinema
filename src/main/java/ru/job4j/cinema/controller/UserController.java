package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/formRegistration")
    public String createUser(Model model, HttpSession session) {
        addUserAttribute(model, session);
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute User user, Model model, HttpServletRequest req) {
        Optional<User> regUser = service.add(user);
        String result = "redirect:/index";
        if (regUser.isEmpty()) {
            model.addAttribute("error", true);
            result = "registration";
        } else {
            HttpSession session = req.getSession();
            session.setAttribute("user", regUser.get());
        }
        return result;
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model,
                            @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        String result = "redirect:/index";
        Optional<User> userDb = service.findUserByEmailAndPwd(user.getEmail(), user.getPassword());
        if (userDb.isEmpty()) {
            result = "redirect:/loginPage?fail=true";
        } else {
            HttpSession session = req.getSession();
            session.setAttribute("user", userDb.get());
        }
        return result;
    }

    private void addUserAttribute(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
    }
}
