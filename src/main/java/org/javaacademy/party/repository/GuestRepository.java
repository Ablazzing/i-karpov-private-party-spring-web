package org.javaacademy.party.repository;

import static java.sql.DriverManager.getConnection;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.javaacademy.party.dto.GuestDto;
import org.javaacademy.party.entity.Guest;
import org.javaacademy.party.exception.NoAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class GuestRepository {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/party";
    private static final String INSERT_QUERY = "INSERT INTO guest (name, email, phone ) VALUES (?, ?, ?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM GUEST";
    private static final String GUEST_NAME_VIEW_QUERY = "SELECT * FROM guest_name";

    public GuestRepository() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(GuestDto guestDto, String dbUser, String dbPassword) {
        try (Connection connection = getConnection(DB_URL, dbUser, dbPassword)) {
            PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, RETURN_GENERATED_KEYS);
            statement.setString(1, guestDto.getName());
            statement.setString(2, guestDto.getEmail());
            statement.setLong(3, guestDto.getPhone());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new NoAccessException(e.getMessage());
        }
    }


    public List<Guest> findAll(String dbUser, String dbPassword) {
        List<Guest> guests = new ArrayList<>();
        Connection connection = createConnection(dbUser, dbPassword);
        try {
            getGuestFromTable(connection, guests);
        } catch (SQLException sqlException) {
            getGuestFromView(connection, guests);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection);
        }
        return guests;
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection createConnection(String dbUser, String dbPassword) {
        try {
            return getConnection(DB_URL, dbUser, dbPassword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void getGuestFromView(Connection connection, List<Guest> guests) {
        try (PreparedStatement statement = connection.prepareStatement(GUEST_NAME_VIEW_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                guests.add(getGuestShort(resultSet));
            }
        } catch (SQLException ex) {
            throw new NoAccessException(ex.getMessage());
        }
    }

    private void getGuestFromTable(Connection connection, List<Guest> guests)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery();) {
            while (resultSet.next()) {
               guests.add(getGuestFull(resultSet));
            }
        }
    }

    private Guest getGuestFull(ResultSet resultSet) throws SQLException {
        Guest guest = new Guest();
        guest.setId(resultSet.getLong("id"));
        guest.setName(resultSet.getString("name"));
        guest.setEmail(resultSet.getString("email"));
        guest.setPhone(resultSet.getLong("phone"));
        return guest;
    }

    private Guest getGuestShort(ResultSet resultSet) throws SQLException {
        Guest guest = new Guest();
        guest.setName(resultSet.getString("name"));
        return guest;
    }
}
