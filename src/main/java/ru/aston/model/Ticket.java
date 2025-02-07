package ru.aston.model;

import java.sql.Timestamp;

public class Ticket implements ParkingObject {
    private int id;
    private int userId;
    private int carId;
    private int parkSpotId;
    private int parkingTimeInHours;
    private Timestamp startOfParking;
    private Timestamp endOfParking;

    public Ticket() {
    }

    @Override
    public String toString() {
        return "\n" + "Ticket{" +
                "id=" + id +
                ", user_id=" + userId +
                ", car_id=" + carId +
                ", park_spot_id=" + parkSpotId +
                ", parking_time_in_hours=" + parkingTimeInHours +
                ", start_of_parking=" + startOfParking +
                ", end_of_parking=" + endOfParking +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getParkSpotId() {
        return parkSpotId;
    }

    public void setParkSpotId(int parkSpotId) {
        this.parkSpotId = parkSpotId;
    }

    public int getParkingTimeInHours() {
        return parkingTimeInHours;
    }

    public void setParkingTimeInHours(int parkingTimeInHourse) {
        this.parkingTimeInHours = parkingTimeInHourse;
        setStartOfParking();
    }

    public Timestamp getStartOfParking() {
        return startOfParking;
    }

    public void setStartOfParking(Timestamp time) {
        this.startOfParking = time;
    }

    public void setStartOfParking() {
        this.startOfParking = new Timestamp(System.currentTimeMillis());
        setEndOfParking(parkingTimeInHours);
    }

    public Timestamp getEndOfParking() {
        return endOfParking;
    }

    public void setEndOfParking(int parkingTimeInHours) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.endOfParking = new Timestamp(timestamp.getTime() + (long) parkingTimeInHours * 60 * 60 * 1000);
    }

    public void setEndOfParking(Timestamp time) {
        this.endOfParking = time;
    }
}
