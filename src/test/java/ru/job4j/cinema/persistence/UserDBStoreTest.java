package ru.job4j.cinema.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.User;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDBStoreTest {

    private UserDBStore userStore;

    @BeforeEach
    public void init() {
        userStore = new UserDBStore(new Main().loadPool());
    }

    @AfterEach
    public void clear() {
        userStore.deleteAll();
    }

    @Test
    public void whenUserAdded() {
        User user = new User(
                1, "Ivan", "email", "000000", "password");
        assertThat(userStore.add(user), is(Optional.of(user)));
    }

    @Test
    public void whenUserCanNotBeAdded() {
        User user = new User(
                1, "Ivan", "email", "000000", "password");
        User otherUser = new User(
                1, "Boris", "email", "000000", "123");
        assertThat(userStore.add(user), is(Optional.of(user)));
        assertThat(userStore.add(otherUser), is(Optional.empty()));
    }

    @Test
    public void whenUserFoundByEmailAndPassword() {
        String email = "email";
        String password = "password";
        User user = new User(1, "Ivan", email, "000000", password);
        userStore.add(user);
        assertThat(userStore
                .findUserByEmailAndPwd(email, password), is(Optional.of(user)));
    }

    @Test
    public void whenPasswordInvalid() {
        String email = "email";
        User user = new User(
                1, "Ivan", email, "000000", "password");
        userStore.add(user);
        assertThat(userStore
                .findUserByEmailAndPwd(email, "123456"), is(Optional.empty()));
    }

    @Test
    public void whenEmailInvalid() {
        String password = "123456";
        User user = new User(1, "Ivan", "email", "000000", password);
        userStore.add(user);
        assertThat(userStore
                .findUserByEmailAndPwd("_@email._", password), is(Optional.empty()));
    }

    @Test
    public void whenUserDeleted() {
        User user = new User(
                1, "Ivan", "email", "000000", "123456");
        Optional<User> optionalUser = userStore.add(user);
        user = optionalUser.isEmpty() ? user : optionalUser.get();
        assertTrue(userStore.delete(user.getId()));
    }

    @Test
    public void whenIdInvalid() {
        User user = new User(
                1, "Ivan", "email", "000000", "123456");
        userStore.add(user);
        assertFalse(userStore.delete(-1));
    }
}
