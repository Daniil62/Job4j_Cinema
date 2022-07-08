package ru.job4j.cinema.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TicketDBStoreTest {

    private UserDBStore userStore;
    private TicketDBStore ticketStore;
    private SessionDBStore sessionStore;
    private User user;
    private Session session;

    @BeforeEach
    public void init() {
        userStore = new UserDBStore(new Main().loadPool());
        ticketStore = new TicketDBStore(new Main().loadPool());
        sessionStore = new SessionDBStore(new Main().loadPool());
        clear();
        user = new User(
                1, "Ivan", "email", "000000", "password");
        session = new Session(1, "Some movie");
        Optional<User> optionalUser = userStore.add(user);
        Optional<Session> optionalSession = sessionStore.add(session);
        session = optionalSession.isEmpty() ? session : optionalSession.get();
        user = optionalUser.isEmpty() ? user : optionalUser.get();
    }

    @AfterEach
    public void postClear() {
        clear();
    }

    private void clear() {
        ticketStore.deleteAll();
        sessionStore.deleteAll();
        userStore.deleteAll();
    }

    @Test
    public void whenTicketAdded() {
        Ticket ticket = new Ticket(1, session, 5, 5, user.getId());
        assertThat(ticketStore.add(ticket), is(Optional.of(ticket)));
    }

    @Test
    public void whenTicketCanNotBeAdded() {
        Ticket ticket = new Ticket(1, session, 5, 5, user.getId());
        Ticket ticket2 = new Ticket(
                1, new Session(2, "Some movie"), 5, 5, user.getId());
        sessionStore.add(session);
        ticketStore.add(ticket);
        assertThat(ticketStore.add(ticket2), is(Optional.empty()));
    }

    @Test
    public void whenTicketsFoundBySessionId() {
        Ticket ticket = new Ticket(1, session, 5, 5, user.getId());
        ticketStore.add(ticket);
        assertThat(ticketStore.findBySessionId(session.getId()), is(List.of(ticket)));
    }

    @Test
    public void whenTicketsNotFoundBySessionId() {
        Ticket ticket = new Ticket(1, session, 5, 5, user.getId());
        ticketStore.add(ticket);
        assertThat(ticketStore.findBySessionId(-1), is(List.of()));
    }

    @Test
    public void whenTicketsFoundByUserId() {
        Ticket ticket = new Ticket(1, session, 5, 5, user.getId());
        Ticket ticket2 = new Ticket(1, session, 5, 6, user.getId());
        ticketStore.add(ticket);
        ticketStore.add(ticket2);
        assertThat(ticketStore.findByUserId(user.getId()), is(List.of(ticket, ticket2)));
    }

    @Test
    public void whenTicketsNotFoundByUserId() {
        Ticket ticket = new Ticket(1, session, 5, 5, user.getId());
        Ticket ticket2 = new Ticket(1, session, 5, 6, user.getId());
        ticketStore.add(ticket);
        ticketStore.add(ticket2);
        assertThat(ticketStore.findByUserId(-1), is(List.of()));
    }

    @Test
    public void whenTicketDeleted() {
        Ticket ticket = new Ticket(1, session, 5, 5, user.getId());
        Optional<Ticket> optionalTicket = ticketStore.add(ticket);
        ticket = optionalTicket.isEmpty() ? ticket : optionalTicket.get();
        assertTrue(ticketStore.delete(ticket.getId()));
    }

    @Test
    public void whenTicketsCanNotBeDeleted() {
        Ticket ticket = new Ticket(1, session, 5, 5, user.getId());
        ticketStore.add(ticket);
        assertFalse(ticketStore.delete(-1));
    }

    @Test
    public void whenTicketsDeletedByUserId() {
        Ticket ticket = new Ticket(1, session, 5, 5, user.getId());
        Ticket ticket2 = new Ticket(1, session, 5, 6, user.getId());
        ticketStore.add(ticket);
        ticketStore.add(ticket2);
        assertTrue(ticketStore.deleteAllTicketsByUserId(user.getId()));
        assertThat(ticketStore.findByUserId(user.getId()), is(List.of()));
    }

    @Test
    public void whenTicketsCanNotBeDeletedByUserId() {
        Ticket ticket = new Ticket(1, session, 5, 5, user.getId());
        Ticket ticket2 = new Ticket(1, session, 5, 6, user.getId());
        ticketStore.add(ticket);
        ticketStore.add(ticket2);
        assertFalse(ticketStore.deleteAllTicketsByUserId(-1));
        assertThat(ticketStore.findByUserId(user.getId()), is(List.of(ticket, ticket2)));
    }
}
