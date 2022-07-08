package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class TicketController {

    private final TicketService ticketService;
    private final SessionService sessionService;

    public TicketController(TicketService ticketService, SessionService sessionService) {
        this.ticketService = ticketService;
        this.sessionService = sessionService;
    }

    @GetMapping("/formBuyTicket/{sessionId}")
    public String formBuyTicket(Model model,
                                HttpSession session,
                                @PathVariable("sessionId") int sessionId) {
        addUserAttribute(model, session);
        Map<Integer, List<Integer>> places = ticketService.getFreePlaces(sessionId);
        boolean noTickets = places.keySet().isEmpty();
        if (!noTickets) {
            model.addAttribute("rows", places.keySet().toArray());
            model.addAttribute("cells",
                    places.get(places.keySet().iterator().next()));
        }
        model.addAttribute("noTickets", noTickets);
        Optional<Session> optionalSession = sessionService.findById(sessionId);
        User user = (User) session.getAttribute("user");
        Ticket ticket = new Ticket();
        ticket.setSession(optionalSession.orElseGet(Session::new));
        ticket.setUserId(user.getId());
        model.addAttribute("ticket", ticket);
        return "buyTicket";
    }

    @PostMapping("/buyTicket")
    public String buyTicket(@ModelAttribute Ticket ticket, Model model, HttpSession session) {
        addUserAttribute(model, session);
        Optional<Ticket> boughtTicket = ticketService.add(ticket);
        String result = "redirect:/tickets";
        boolean isItFail = boughtTicket.isEmpty();
        if (isItFail) {
            result = "buyTicket";
            Map<Integer, List<Integer>> places =
                    ticketService.getFreePlaces(ticket.getSession().getId());
            model.addAttribute("fail", true);
            model.addAttribute("rows", places.keySet().toArray());
            model.addAttribute("cells", places.get(places.keySet().iterator().next()));
        }
        model.addAttribute("noTickets", false);
        return result;
    }

    @GetMapping("tickets")
    public String tickets(Model model, HttpSession session) {
        addUserAttribute(model, session);
        User user = (User) session.getAttribute("user");
        List<Ticket> myTickets = ticketService.findByUserId(user.getId());
        myTickets.forEach(t -> t.setSession(sessionService
                .findById(t.getSession().getId()).orElseGet(Session::new)));
        model.addAttribute("tickets", myTickets);
        return "tickets";
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
