package com.traffic.db;

import com.traffic.model.ViolationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViolationRepository extends JpaRepository<ViolationRecord, Long> {

    // Search by vehicle ID (case-insensitive partial match)
    List<ViolationRecord> findByVehicleIdContainingIgnoreCase(String vehicleId);

    // All violations ordered newest first
    List<ViolationRecord> findAllByOrderByTimestampDesc();
}
