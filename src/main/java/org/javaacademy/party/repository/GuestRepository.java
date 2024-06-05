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
@RequiredArgsConstructor
public class GuestRepository {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/party";
    private static final String INSERT_QUERY = "INSERT INTO guest (name, email, phone ) VALUES (?, ?, ?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM GUEST";
    private static final String GUEST_NAME_VIEW_QUERY = "SELECT * FROM guest_name";

    public Guest save(GuestDto guestDto, String dbUser, String dbPassword) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try (Connection connection = getConnection(DB_URL, dbUser, dbPassword)) {
            PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, RETURN_GENERATED_KEYS);
            statement.setString(1, guestDto.getName());
            statement.setString(2, guestDto.getEmail());
            statement.setLong(3, guestDto.getPhone());
            int insertedRows = statement.executeUpdate();
            if (insertedRows == 0) {
                throw new SQLException("Creating guest failed, no rows inserted.");
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Guest guest = new Guest();
                guest.setId(generatedKeys.getLong(1));
                guest.setName(guestDto.getName());
                guest.setEmail(guestDto.getEmail());
                guest.setPhone(guestDto.getPhone());
                return guest;
            } else {
                throw new SQLException("Creating guest failed, no ID received.");
            }
        } catch (SQLException e) {
            throw new NoAccessException(e.getMessage());
        }
    }

    public List<Guest> findAll(String dbUser, String dbPassword) throws ClassNotFoundException {
        List<Guest> guests = new ArrayList<>();
        Class.forName("org.postgresql.Driver");
        try (Connection connection = getConnection(DB_URL, dbUser, dbPassword)) {
            try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Guest guest = new Guest();
                    guest.setId(resultSet.getLong("id"));
                    guest.setName(resultSet.getString("name"));
                    guest.setEmail(resultSet.getString("email"));
                    guest.setPhone(resultSet.getLong("phone"));
                    guests.add(guest);
                }
            } catch (SQLException e) {
                try (PreparedStatement statement = connection.prepareStatement(GUEST_NAME_VIEW_QUERY);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        Guest guest = new Guest();
                        guest.setName(resultSet.getString("name"));
                        guests.add(guest);
                    }
                } catch (SQLException ex) {
                    throw new NoAccessException(ex.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new NoAccessException(e.getMessage());
        }
        return guests;
    }
}
