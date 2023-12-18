package com.example.airport.dao;

import com.example.airport.model.Departure;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class PostgresDao {
    private static final String INSERT_DEPARTURE_SQL =
            "INSERT INTO departures (dep_iata, arr_iata, dep_time, aircraft_icao) VALUES (?, ?, ?, ?)";

    private static final String INSERT_DISTRIBUTION_SQL =
            "INSERT INTO distributions (dep_iata, dep_time, count) VALUES (?, ?, ?)";

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
        }
    }

    public void saveDistribution(String depIata, Map<LocalDateTime, Integer> distribution) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DISTRIBUTION_SQL)) {

            for (Map.Entry<LocalDateTime, Integer> entry : distribution.entrySet()) {
                preparedStatement.setString(1, depIata);
                preparedStatement.setObject(2, entry.getKey());
                preparedStatement.setInt(3, entry.getValue());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
