package model;

public class ParkingSpot {
    private final String spotId;
    private final VehicleType vehicleType;
    private boolean occupied;

    public ParkingSpot(String spotId, VehicleType vehicleType) {
        this.spotId = spotId;
        this.vehicleType = vehicleType;
        this.occupied = false;
    }

    public synchronized void occupySpot() {
        this.occupied = true;
    }

    public synchronized void releaseSpot() {
        this.occupied = false;
    }

    public String getSpotId() {
        return spotId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public synchronized boolean isOccupied() {
        return occupied;
    }

    public synchronized void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    @Override
    public String toString() {
        return "ParkingSpot{" +
                "spotId='" + spotId + '\'' +
                ", vehicleType=" + vehicleType +
                ", occupied=" + occupied +
                '}';
    }
}
