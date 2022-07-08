package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Test
    public void whenCreateUser() {
        UserService service = mock(UserService.class);
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        assertThat(new UserController(service)
                .createUser(model, httpSession), is("registration"));
    }

    @Test
    public void whenRegistration() {
        User user = new User(
                1, "Ivan", "mail.ru", "0000000", "password");
        UserService service = mock(UserService.class);
        Model model = mock(Model.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(service.add(user)).thenReturn(Optional.of(user));
        when(request.getSession()).thenReturn(httpSession);
        assertThat(new UserController(service)
                .registration(user, model, request), is("redirect:/index"));
    }

    @Test
    public void whenCanNotRegistration() {
        User user = new User(
                1, "Ivan", "mail.ru", "0000000", "password");
        UserService service = mock(UserService.class);
        Model model = mock(Model.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(service.add(user)).thenReturn(Optional.empty());
        when(request.getSession()).thenReturn(httpSession);
        assertThat(new UserController(service)
                .registration(user, model, request), is("registration"));
    }

    @Test
    public void whenLoginPAge() {
        UserService service = mock(UserService.class);
        Model model = mock(Model.class);
        assertThat(new UserController(service).loginPage(model, false), is("login"));
    }

    @Test
    public void whenLogin() {
        User user = new User(
                1, "Ivan", "mail.ru", "0000000", "password");
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserService service = mock(UserService.class);
        when(service.findUserByEmailAndPwd(user.getEmail(), user.getPassword()))
                .thenReturn(Optional.of(user));
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        assertThat(new UserController(service)
                .login(user, request), is("redirect:/index"));
    }

    @Test
    public void whenCanNotLogin() {
        User user = new User(
                1, "Ivan", "mail.ru", "0000000", "password");
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserService service = mock(UserService.class);
        when(service.findUserByEmailAndPwd(user.getEmail(), user.getPassword()))
                .thenReturn(Optional.empty());
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        assertThat(new UserController(service)
                .login(user, request), is("redirect:/loginPage?fail=true"));
    }
}
