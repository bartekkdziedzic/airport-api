package com.example.airport.dao;

import com.example.airport.model.Departure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DepartureDao {
    private static final String INSERT_DEPARTURE_SQL =
            "INSERT INTO departures (dep_iata, arr_iata, dep_time, aircraft_icao) VALUES (?, ?, ?, ?)";

    public void saveDepartures(List<Departure> departures) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DEPARTURE_SQL)) {

            for (Departure departure : departures) {
                preparedStatement.setString(1, departure.dep_iata());
                preparedStatement.setString(2, departure.arr_iata());
                preparedStatement.setString(3, departure.dep_time());
                preparedStatement.setString(4, departure.aircraft_icao());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
    }
}
