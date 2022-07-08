package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class TicketControllerTest {

    @Test
    public void whenFormBuyTicket() {
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        TicketService ticketService = mock(TicketService.class);
        SessionService sessionService = mock(SessionService.class);
        when(ticketService.getFreePlaces(1)).thenReturn(Map.of(1, List.of(1, 2)));
        when(sessionService.findById(1)).thenReturn(Optional.of(new Session(1, "Some movie")));
        when(httpSession.getAttribute("user"))
                .thenReturn(new User(
                        1, "Ivan", "mail.ru", "0000000", "password"));
        assertThat(new TicketController(ticketService, sessionService)
                        .formBuyTicket(model, httpSession, 1), is("buyTicket"));
    }

    @Test
    public void whenBuyTicket() {
        Ticket ticket = new Ticket(1, new Session(
                1, "Some movie"), 5, 5, 1);
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        TicketService ticketService = mock(TicketService.class);
        SessionService sessionService = mock(SessionService.class);
        when(ticketService.add(ticket)).thenReturn(Optional.of(ticket));
        assertThat(new TicketController(ticketService, sessionService)
                .buyTicket(ticket, model, httpSession), is("redirect:/tickets"));
    }


    @Test
    public void whenCanNotBuyTicket() {
        Ticket ticket = new Ticket(1, new Session(
                1, "Some movie"), 5, 5, 1);
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        TicketService ticketService = mock(TicketService.class);
        SessionService sessionService = mock(SessionService.class);
        when(ticketService.add(ticket)).thenReturn(Optional.empty());
        when(ticketService.getFreePlaces(ticket.getSession().getId()))
                .thenReturn(Map.of(1, List.of(5, 6)));
        assertThat(new TicketController(ticketService, sessionService)
                .buyTicket(ticket, model, httpSession), is("buyTicket"));
    }

    @Test
    public void whenTickets() {
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        TicketService ticketService = mock(TicketService.class);
        SessionService sessionService = mock(SessionService.class);
        User user = new User(1, "Ivan", "mail.ru", "0000000", "password");
        when(httpSession.getAttribute("user")).thenReturn(user);
        when(ticketService.findByUserId(user.getId()))
                .thenReturn(List.of(new Ticket(1, new Session(
                                1, "Some movie"), 5, 5, 1)));
        assertThat(new TicketController(ticketService, sessionService)
                .tickets(model, httpSession), is("tickets"));
    }
}
