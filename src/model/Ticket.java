package model;

import java.util.Date;

public class Ticket {
    private final String ticketID;
    private final Vehicle vehicle;
    private final ParkingSpot parkingSpot;
    private final Date entryTime;
    private Date exitTime;
    private double fee;
    private TicketStatus status;

    public Ticket(String ticketID, Vehicle vehicle, ParkingSpot parkingSpot) {
        this.ticketID = ticketID;
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.entryTime = new Date();
        this.exitTime = null;
        this.fee = 0.0;
        this.status = TicketStatus.ACTIVE;
        this.parkingSpot.occupySpot();
    }

    public String getTicketID() {
        return ticketID;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public double getFee() {
        return fee;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public synchronized void closeTicket(double fee, Date exitTime) {
        if (this.status == TicketStatus.CLOSED) {
            return;
        }
        this.exitTime = exitTime;
        this.fee = fee;
        this.status = TicketStatus.CLOSED;
        if (parkingSpot != null) {
            parkingSpot.releaseSpot();
        }
    }

    public synchronized void closeTicket(double fee) {
        closeTicket(fee, new Date());
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketID='" + ticketID + '\'' +
                ", vehicle=" + vehicle +
                ", entryTime=" + entryTime +
                ", exitTime=" + exitTime +
                ", fee=" + fee +
                ", status=" + status +
                ", parkingSpot=" + parkingSpot.getSpotId() +
                '}';
    }
}
