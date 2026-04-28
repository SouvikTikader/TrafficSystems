package com.traffic.controller;

import com.traffic.model.VehicleEvent;
import com.traffic.model.ViolationRecord;
import com.traffic.service.TrafficService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TrafficController {

    private final TrafficService service;

    public TrafficController(TrafficService service) {
        this.service = service;
    }

    /** POST /api/process — Submit a vehicle event */
    @PostMapping("/process")
    public ResponseEntity<?> processEvent(@RequestBody VehicleEvent event) {
        if (event.getVehicleId() == null || event.getVehicleId().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Vehicle ID is required"));
        }
        if (event.getZone() == null || event.getZone().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Zone is required"));
        }

        Optional<ViolationRecord> result = service.processEvent(event);
        if (result.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "violation", true,
                    "record", result.get()
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                    "violation", false,
                    "message", "No violation detected for " + event.getVehicleId()
            ));
        }
    }

    /** GET /api/violations — All violations */
    @GetMapping("/violations")
    public List<ViolationRecord> getAllViolations() {
        return service.getAllViolations();
    }

    /** GET /api/violations/search?q=MH12 — Search by vehicle ID */
    @GetMapping("/violations/search")
    public List<ViolationRecord> search(@RequestParam String q) {
        return service.searchViolations(q);
    }

    /** DELETE /api/violations/{id} — Delete a violation */
    @DeleteMapping("/violations/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (service.deleteViolation(id)) {
            return ResponseEntity.ok(Map.of("deleted", true));
        }
        return ResponseEntity.notFound().build();
    }

    /** GET /api/stats — Dashboard stats */
    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return Map.of(
                "totalViolations", service.getTotalViolations(),
                "totalFines", service.getTotalFines()
        );
    }
}
