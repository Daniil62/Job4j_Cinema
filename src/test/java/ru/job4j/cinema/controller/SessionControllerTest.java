package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.service.SessionService;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class SessionControllerTest {

    @Test
    public void whenShowSessions() {
        List<Session> sessions = List.of(new Session(1, "some movie"));
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        SessionService service = mock(SessionService.class);
        when(service.getAll()).thenReturn(sessions);
        assertThat(new SessionController(service).showSessions(model, httpSession), is("index"));
    }
}
