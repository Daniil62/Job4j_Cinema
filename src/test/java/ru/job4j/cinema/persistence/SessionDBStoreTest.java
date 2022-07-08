package ru.job4j.cinema.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.Session;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SessionDBStoreTest {

    private SessionDBStore store;

    @BeforeEach
    public void init() {
        store = new SessionDBStore(new Main().loadPool());
        store.deleteAll();
    }

    @Test
    public void whenSessionAdded() {
        Session session = new Session(1, "Some movie");
        assertThat(store.add(session), is(Optional.of(session)));
    }

    @Test
    public void whenSessionCanNotBeAdded() {
        Session session = new Session(1, "Some movie");
        store.add(session);
        assertThat(store.add(session), is(Optional.empty()));
    }

    @Test
    public void whenGetAll() {
        Session someMovie = new Session(1, "Some movie");
        Session someMovie2 = new Session(2, "Some movie 2");
        store.add(someMovie);
        store.add(someMovie2);
        assertThat(store.getAll(), is(List.of(someMovie, someMovie2)));
    }

    @Test
    public void whenSessionFoundById() {
        Session someMovie = new Session(1, "Some movie");
        Optional<Session> optionalSession = store.add(someMovie);
        int id = optionalSession.isEmpty() ? 0 : optionalSession.get().getId();
        assertThat(store.findById(id), is(optionalSession));
    }

    @Test
    public void whenSessionNotFoundByIdBecauseIdInvalid() {
        store.add(new Session(1, "Some movie"));
        assertThat(store.findById(-1), is(Optional.empty()));
    }

    @Test
    public void whenSessionDeleted() {
        Optional<Session> optionalSession = store.add(new Session(1, "Some movie"));
        int id = optionalSession.isEmpty() ? 0 : optionalSession.get().getId();
        assertThat(store.findById(id), is(optionalSession));
        assertTrue(store.delete(id));
        assertThat(store.findById(id), is(Optional.empty()));
    }

    @Test
    public void whenSessionCanNotBeDeletedBecauseIdInvalid() {
        Session someMovie = new Session(1, "Some movie");
        store.add(someMovie);
        assertFalse(store.delete(-1));
    }
}
