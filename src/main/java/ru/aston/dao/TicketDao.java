package ru.aston.dao;

import ru.aston.db.DBConnection;
import ru.aston.model.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketDao implements SimpleDao<Ticket> {
    public static final String GET_ALL = "SELECT * FROM tickets";
    public static final String FIND_BY_ID = "SELECT * FROM tickets WHERE id = ?";
    private static final String INSERT_TICKET = "INSERT INTO tickets(user_id, car_id, park_spot_id, parking_time_in_hours, start_of_parking, end_of_parking) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String DELETE_TICKET = "DELETE FROM tickets WHERE id = ?";

    private final Connection connection;

    public TicketDao() {
        try {
            connection = DBConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Ticket getTicket(ResultSet resultSet) throws SQLException {
        Ticket ticket = new Ticket();

        ticket.setId(resultSet.getInt("id"));
        ticket.setUserId(resultSet.getInt("user_id"));
        ticket.setCarId(resultSet.getInt("car_id"));
        ticket.setParkSpotId(resultSet.getInt("park_spot_id"));
        ticket.setParkingTimeInHours(resultSet.getInt("parking_time_in_hours"));
        ticket.setStartOfParking(resultSet.getTimestamp("start_of_parking"));
        ticket.setEndOfParking(resultSet.getTimestamp("end_of_parking"));
        return ticket;
    }

    @Override
    public void deleteById(int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TICKET)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Ticket record) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TICKET)) {

            preparedStatement.setInt(1, record.getUserId());
            preparedStatement.setInt(2, record.getCarId());
            preparedStatement.setInt(3, record.getParkSpotId());
            preparedStatement.setInt(4, record.getParkingTimeInHours());
            preparedStatement.setTimestamp(5, record.getStartOfParking());
            preparedStatement.setTimestamp(6, record.getEndOfParking());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Ticket ticket = getTicket(resultSet);
                tickets.add(ticket);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return tickets;
    }

    @Override
    public Optional<Ticket> findById(int id) {
        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Ticket ticket = getTicket(resultSet);
                    return Optional.of(ticket);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to query Ticket by Id: " + e.getMessage());
        }
        return Optional.empty();
    }
}