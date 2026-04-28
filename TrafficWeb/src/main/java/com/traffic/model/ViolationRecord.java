package com.traffic.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "violations")
public class ViolationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String vehicleId;

    private double speed;
    private String zone;
    private int fine;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public ViolationRecord() {}

    public ViolationRecord(String vehicleId, double speed, String zone, int fine) {
        this.vehicleId = vehicleId;
        this.speed = speed;
        this.zone = zone;
        this.fine = fine;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public Long getId()            { return id; }
    public String getVehicleId()   { return vehicleId; }
    public double getSpeed()       { return speed; }
    public String getZone()        { return zone; }
    public int getFine()           { return fine; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Setters
    public void setId(Long id)             { this.id = id; }
    public void setVehicleId(String v)     { this.vehicleId = v; }
    public void setSpeed(double s)         { this.speed = s; }
    public void setZone(String z)          { this.zone = z; }
    public void setFine(int f)             { this.fine = f; }
    public void setTimestamp(LocalDateTime t) { this.timestamp = t; }

    @Override
    public String toString() {
        return String.format("[%d] %s | %.1f km/h | %s | ₹%d | %s",
                id, vehicleId, speed, zone, fine,
                timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
