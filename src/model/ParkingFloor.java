package model;

import java.util.ArrayList;
import java.util.List;

public class ParkingFloor {
    private final int floorNumber;
    private final List<ParkingSpot> parkingSpots;

    public ParkingFloor(int floorNumber, List<ParkingSpot> parkingSpots) {
        this.floorNumber = floorNumber;
        this.parkingSpots = new ArrayList<>(parkingSpots);
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public synchronized List<ParkingSpot> getParkingSpots() {
        return new ArrayList<>(parkingSpots);
    }

    public synchronized void addSpot(ParkingSpot spot) {
        parkingSpots.add(spot);
    }

    public synchronized int getAvailableSpotsCount() {
        return (int) parkingSpots.stream().filter(spot -> !spot.isOccupied()).count();
    }
}
