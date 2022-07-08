package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.persistence.SessionDBStore;

import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    private final SessionDBStore store;

    public SessionService(SessionDBStore store) {
        this.store = store;
    }

    public void addSession(Session session) {
        store.add(session);
    }

    public List<Session> getAll() {
        return store.getAll();
    }

    public Optional<Session> findById(int id) {
        return store.findById(id);
    }

    public boolean delete(int id) {
        return store.delete(id);
    }

    public void deleteAll() {
        store.getAll();
    }
}
