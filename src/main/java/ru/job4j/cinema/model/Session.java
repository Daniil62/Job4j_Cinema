package ru.job4j.cinema.model;

import java.util.Objects;

public class Session {

    private int id;
    private String name;
    private byte[] poster;

    public Session() {
    }

    public Session(int id) {
        this.id = id;
    }

    public Session(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Session(int id, String name, byte[] poster) {
        this.id = id;
        this.name = name;
        this.poster = poster;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Session)) {
            return false;
        }
        return id == ((Session) o).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String n = System.lineSeparator();
        return "id: " + id + n + "name: " + name + n;
    }
}
