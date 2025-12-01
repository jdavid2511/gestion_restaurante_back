package com.prueba.gestion_restaurante_back.model.customer;

public enum RangeLevel {
    BRONZE(5),
    PLATA(10),
    ORO(15);

    private final int pointsPerReservation;

    RangeLevel(int pointsPerReservation) {
        this.pointsPerReservation = pointsPerReservation;
    }

    public int getPointsPerReservation() {
        return pointsPerReservation;
    }
}
