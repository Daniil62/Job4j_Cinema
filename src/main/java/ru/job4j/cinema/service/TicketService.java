package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.persistence.TicketDBStore;

import java.util.*;

@Service
public class TicketService {

    private final TicketDBStore store;
    private static final int ROWS = 10;
    private static final int CELLS = 10;

    public TicketService(TicketDBStore store) {
        this.store = store;
    }

    public Optional<Ticket> add(Ticket ticket) {
        return store.add(ticket);
    }

    public List<Ticket> findBySessionId(int sessionId) {
       return store.findBySessionId(sessionId);
    }

    public List<Ticket> findByUserId(int userId) {
       return store.findByUserId(userId);
    }

    public boolean deleteTicket(int id) {
        return store.delete(id);
    }

    public boolean deleteTicketsByUserId(int userId) {
        return store.deleteAllTicketsByUserId(userId);
    }

    public Map<Integer, List<Integer>> getFreePlaces(int sessionId) {
        Map<Integer, List<Integer>> result = new HashMap<>();
        List<Ticket> occupiedPlaces = findBySessionId(sessionId);
        for (int row = 1; row <= ROWS; row++) {
            for (int cell = 1; cell <= CELLS; cell++) {
                result.putIfAbsent(row, new ArrayList<>());
                Ticket ticket = new Ticket(new Session(sessionId), row, cell);
                if (!occupiedPlaces.contains(ticket)) {
                    result.get(row).add(cell);
                }
            }
            if (result.get(row).isEmpty()) {
                result.remove(row);
            }
        }
        return result;
    }
}
