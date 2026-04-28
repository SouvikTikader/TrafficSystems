package com.traffic.service;

import com.traffic.db.ViolationRepository;
import com.traffic.model.VehicleEvent;
import com.traffic.model.ViolationRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class TrafficService {

    private final ViolationRepository repo;

    // Same lambda logic from original code
    private static final Predicate<VehicleEvent> violationFilter =
            event -> event.getSpeed() > 80 && !event.isEmergencyVehicle();

    private static final Function<Double, Integer> fineCalculator = speed -> {
        if (speed > 120) return 5000;
        else if (speed > 100) return 2000;
        else return 1000;
    };

    public TrafficService(ViolationRepository repo) {
        this.repo = repo;
    }

    /**
     * Processes a vehicle event. If it's a violation, saves to DB and returns the record.
     * Returns empty Optional if no violation.
     */
    public Optional<ViolationRecord> processEvent(VehicleEvent event) {
        if (!violationFilter.test(event)) {
            return Optional.empty();
        }
        String vid   = Optional.ofNullable(event.getVehicleId()).orElse("Unknown").toUpperCase();
        String zone  = Optional.ofNullable(event.getZone()).orElse("Unknown").toUpperCase();
        int fine     = fineCalculator.apply(event.getSpeed());

        ViolationRecord record = new ViolationRecord(vid, event.getSpeed(), zone, fine);
        return Optional.of(repo.save(record));
    }

    public List<ViolationRecord> getAllViolations() {
        return repo.findAllByOrderByTimestampDesc();
    }

    public List<ViolationRecord> searchViolations(String vehicleId) {
        return repo.findByVehicleIdContainingIgnoreCase(vehicleId);
    }

    public boolean deleteViolation(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public long getTotalFines() {
        return repo.findAll().stream().mapToLong(ViolationRecord::getFine).sum();
    }

    public long getTotalViolations() {
        return repo.count();
    }
}
