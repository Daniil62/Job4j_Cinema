package ru.job4j.cinema.model;

import java.util.Objects;

public class Ticket {

    private int id;
    private Session session;
    private int row;
    private int cell;
    private int userId;

    public Ticket() {
    }

    public Ticket(Session session, int row, int cell) {
        this.session = session;
        this.row = row;
        this.cell = cell;
    }

    public Ticket(int id, Session session, int row, int cell, int userId) {
        this.id = id;
        this.session = session;
        this.row = row;
        this.cell = cell;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ticket)) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return session.equals(((Ticket) o).session)
                && row == ticket.row
                && cell == ticket.cell;
    }

    @Override
    public int hashCode() {
        int result = session.hashCode();
        result *= 31 + Objects.hash(row);
        result *= 31 + Objects.hash(cell);
        return result;
    }

    @Override
    public String toString() {
        String n = System.lineSeparator();
        return "id: " + id + n
                + "session: " + n + session.toString()
                + "row: " + row + n
                + "cell: " + cell + n
                + "userId: " + userId + n;
    }
}
