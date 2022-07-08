package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class TicketDBStore {

    private final BasicDataSource pool;
    private static Logger log = Logger.getLogger(TicketDBStore.class.getName());

    public TicketDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<Ticket> add(Ticket ticket) {
        Optional<Ticket> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO tickets(session_id, pos_row, cell, user_id) "
                             + "VALUES(?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ticket.getSession().getId());
            ps.setInt(2, ticket.getRow());
            ps.setInt(3, ticket.getCell());
            ps.setInt(4, ticket.getUserId());
            if (ps.executeUpdate() > 0) {
                try (ResultSet id = ps.getGeneratedKeys()) {
                    if (id.next()) {
                        ticket.setId(id.getInt(1));
                    }
                }
                result = Optional.of(ticket);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "error: ", e);
        }
        return result;
    }

    public List<Ticket> findBySessionId(int sessionId) {
        List<Ticket> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM tickets WHERE session_id = ?")) {
            ps.setInt(1, sessionId);
            result = fetchTickets(ps);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "error: ", e);
        }
        return result;
    }

    public List<Ticket> findByUserId(int userId) {
        List<Ticket> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM tickets WHERE user_id = ?")) {
            ps.setInt(1, userId);
            result = fetchTickets(ps);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "error: ", e);
        }
        return result;
    }

    public boolean delete(int id) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "DELETE FROM tickets WHERE id = ?")
        ) {
            ps.setInt(1, id);
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            log.log(Level.SEVERE, "error: ", e);
        }
        return result;
    }

    public boolean deleteAllTicketsByUserId(int userId) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "DELETE FROM tickets WHERE user_id = ?")
        ) {
            ps.setInt(1, userId);
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            log.log(Level.SEVERE, "error: ", e);
        }
        return result;
    }

    public void deleteAll() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM tickets")) {
            ps.execute();
        } catch (Exception e) {
            log.log(Level.SEVERE, "error: ", e);
        }
    }

    private List<Ticket> fetchTickets(PreparedStatement ps) throws SQLException {
        List<Ticket> result = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new Ticket(rs.getInt("id"),
                        new Session(rs.getInt("session_id")),
                        rs.getInt("pos_row"),
                        rs.getInt("cell"),
                        rs.getInt("user_id")));
            }
        }
        return result;
    }
}
