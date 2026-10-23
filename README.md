# Smart Parking Lot System

A lightweight Java-based low-level design for a smart parking lot backend. The project models a multi-floor parking lot, vehicle check-in/check-out, type-based spot allocation, and vehicle-specific fee calculation.

## Project goal

The system is designed to satisfy the core functional requirements described in the brief:
- assign parking spots automatically based on vehicle size
- track the time each vehicle enters and exits
- calculate fees based on duration and vehicle type
- update spot availability in real time
- support multiple vehicles entering/exiting through a synchronized manager

## Architecture overview

The project is organized around a few key building blocks:

- `ParkingLotManager`
  - the central service responsible for managing entry/exit operations
  - handles spot assignment and fee calculation
  - uses a lock for safe concurrent updates

- `ParkingLot`
  - represents the full parking facility
  - contains multiple floors
  - exposes availability counts for overall and per-vehicle-type usage

- `ParkingFloor`
  - contains a list of parking spots on a specific level

- `ParkingSpot`
  - represents a single allocated parking space
  - stores the slot type and occupancy state

- `Vehicle`
  - models a vehicle with a number and `VehicleType`

- `Ticket`
  - records entry time, exit time, assigned spot, fee, and ticket status

- Fee strategy classes
  - `TwoWheelerFeeCalculationStrategy`
  - `FourWheelerFeeCalculationStrategy`
  - `BusFeeCalculationStrategy`
  - `DefaultCalculationStrategy`

- Allocation strategy
  - `DefaultSpotAllocationStrategy`
  - selects the first matching free spot for the vehicle type

## Requirement mapping

### 1) Parking Spot Allocation
Requirement: Automatically assign an available parking spot to a vehicle when it enters, based on the vehicle’s size.

Implementation:
- `ParkingLotManager.checkInVehicle(...)` calls the allocation strategy.
- `DefaultSpotAllocationStrategy.assignSpot(...)` scans the parking lot for a free spot whose `VehicleType` matches the incoming vehicle.
- Once found, it marks the spot as occupied.

Why it matches:
- The system supports type-based assignment for:
  - `TWO_WHEELER`
  - `FOUR_WHEELER`
  - `BUS`

### 2) Check-In and Check-Out
Requirement: Record entry and exit times of vehicles.

Implementation:
- `Ticket` stores `entryTime` when created in the constructor.
- `ParkingLotManager.checkOutVehicle(...)` sets `exitTime` before finalizing the transaction.
- `TicketStatus` tracks whether a ticket is still active or already closed.

Why it matches:
- Each parked vehicle gets a ticket with a unique ID.
- The system can identify and close a vehicle’s session using the ticket ID.

### 3) Parking Fee Calculation
Requirement: Calculate fees based on the duration of stay and vehicle type.

Implementation:
- Each vehicle type uses a dedicated `FeeCalculationStrategy`.
- `ParkingLotManager` chooses the correct strategy from a map keyed by `VehicleType`.
- Fee is computed using actual duration in minutes and each strategy’s pricing logic.

Examples:
- Two-wheelers: low base fee with hourly increment
- Four-wheelers: higher base and hourly rate
- Buses: higher fixed pricing model

Why it matches:
- Fees vary by both time and vehicle type, which is what the brief requires.

### 4) Real-Time Availability Update
Requirement: Update availability of parking spots in real time as vehicles enter and leave.

Implementation:
- `ParkingSpot` has an `occupied` flag.
- `occupySpot()` and `releaseSpot()` update that state immediately.
- `ParkingLot.getAvailableSpotsCount()` and `getAvailableSpotsCountForType(...)` calculate live availability.
- `ParkingLotManager.printAvailability()` prints the current occupancy snapshot.

Why it matches:
- As soon as a vehicle checks in or out, the occupancy state changes.
- The system can report the current free-space status immediately.

### 5) Data Model Considerations
Requirement: Design a database schema to manage parking spots, vehicles, and parking transactions.

Implementation in this codebase:
- The model layer represents the logical entities:
  - `Vehicle`
  - `ParkingSpot`
  - `Ticket`
  - `ParkingLot`
  - `ParkingFloor`

Why it aligns conceptually:
- This is the in-memory equivalent of a data model that would map to a relational schema in a real production system.
- In a DB-backed version, these classes would map naturally to tables like:
  - vehicles
  - parking_spots
  - parking_floors
  - tickets

### 6) Concurrency Handling
Requirement: Ensure the system can handle multiple vehicles entering or exiting simultaneously.

Implementation:
- `ParkingLotManager` uses a single `lock` object around check-in and check-out operations.
- This serializes critical sections so a spot cannot be allocated twice at the same time.
- `activeTickets` is stored in a `ConcurrentHashMap` to support safe concurrent access.
- `ParkingSpot` uses synchronized methods for occupancy changes.

Why it matches:
- The design prevents race conditions during simultaneous entry and exit requests.
- This is a solid lightweight concurrency model for a low-level Java backend prototype.

## Example flow

The `Main` class demonstrates a sample workflow:
- configure a parking lot with multiple floors
- check in several vehicles
- inspect availability
- check out one or more vehicles
- print final availability

This gives a simple end-to-end example of the core system behavior without needing a database or external service.

## Notes

This project is a low-level design prototype in Java. It intentionally keeps the architecture simple and readable while modeling the required business rules. It is suitable for understanding the core logic of a smart parking system and can be extended into a fuller production backend with:
- database persistence
- REST or gRPC APIs
- real-time event streaming
- more advanced allocation algorithms
- richer concurrency and transaction handling

## Summary

The implementation satisfies the brief at the design and logic level:
- multi-floor parking model
- vehicle type based allocation
- time-based ticket tracking
- per-type fee calculation
- real-time availability updates
- synchronized concurrent access



