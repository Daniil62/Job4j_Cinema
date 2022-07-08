package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class SessionDBStore {

    private final BasicDataSource pool;
    private static Logger log = Logger.getLogger(SessionDBStore.class.getName());

    public SessionDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<Session> add(Session session) {
        Optional<Session> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO sessions("
                             + "name, poster) "
                             + "VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, session.getName());
            ps.setBytes(2, session.getPoster());
            if (ps.executeUpdate() > 0) {
                try (ResultSet id = ps.getGeneratedKeys()) {
                    if (id.next()) {
                        session.setId(id.getInt(1));
                    }
                }
                result = Optional.of(session);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "error: ", e);
        }
        return result;
    }

    public List<Session> getAll() {
        List<Session> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM sessions")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Session(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getBytes("poster")));
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "error: ", e);
        }
        return result;
    }

    public Optional<Session> findById(int id) {
        Optional<Session> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM sessions WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result = Optional.of(new Session(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getBytes("poster")));
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "error: ", e);
        }
        return result;
    }

    public boolean delete(int id) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("DELETE FROM sessions WHERE id = ?")
        ) {
            ps.setInt(1, id);
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            log.log(Level.SEVERE, "error: ", e);
        }
        return result;
    }

    public void deleteAll() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM sessions")) {
            ps.execute();
        } catch (Exception e) {
            log.log(Level.SEVERE, "error: ", e);
        }
    }
}