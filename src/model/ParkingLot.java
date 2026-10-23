package model;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private final List<ParkingFloor> parkingFloors;

    public ParkingLot() {
        this.parkingFloors = new ArrayList<>();
    }

    public synchronized void addFloor(ParkingFloor parkingFloor) {
        this.parkingFloors.add(parkingFloor);
    }

    public synchronized List<ParkingFloor> getParkingFloors() {
        return new ArrayList<>(parkingFloors);
    }

    public synchronized int getAvailableSpotsCount() {
        return parkingFloors.stream()
                .flatMap(floor -> floor.getParkingSpots().stream())
                .filter(spot -> !spot.isOccupied())
                .mapToInt(spot -> 1)
                .sum();
    }

    public synchronized int getAvailableSpotsCountForType(VehicleType type) {
        return parkingFloors.stream()
                .flatMap(floor -> floor.getParkingSpots().stream())
                .filter(spot -> spot.getVehicleType() == type)
                .filter(spot -> !spot.isOccupied())
                .mapToInt(spot -> 1)
                .sum();
    }
}
