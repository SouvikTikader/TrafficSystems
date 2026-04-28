package com.traffic.model;

public class VehicleEvent {
    private String vehicleId;
    private double speed;
    private String zone;
    private boolean emergencyVehicle;

    public VehicleEvent() {}

    public String getVehicleId()        { return vehicleId; }
    public double getSpeed()            { return speed; }
    public String getZone()             { return zone; }
    public boolean isEmergencyVehicle() { return emergencyVehicle; }

    public void setVehicleId(String v)      { this.vehicleId = v; }
    public void setSpeed(double s)          { this.speed = s; }
    public void setZone(String z)           { this.zone = z; }
    public void setEmergencyVehicle(boolean e) { this.emergencyVehicle = e; }
}
