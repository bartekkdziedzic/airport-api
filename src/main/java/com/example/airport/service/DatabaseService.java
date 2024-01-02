package com.example.airport.service;

import com.example.airport.model.Departure;
import com.example.airport.model.Distribution;
import com.example.airport.repository.DepartureRepository;
import com.example.airport.repository.DistributionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {
    private final DepartureRepository departureRepository;
    private final DistributionRepository distributionRepository;

    public DatabaseService(DepartureRepository departureRepository, DistributionRepository distributionRepository) {
        this.departureRepository = departureRepository;
        this.distributionRepository = distributionRepository;
    }

    public void saveDepartures(List<Departure> departures) {
        departureRepository.saveAll(departures);
    }


    public void saveDistribution(String depIata, Map<LocalDateTime, Integer> distribution) {
        Distribution distributionEntity = new Distribution(depIata, LocalDateTime.now(), distribution);

        distributionRepository.save(distributionEntity);
    }


}
