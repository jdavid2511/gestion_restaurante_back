package com.prueba.gestion_restaurante_back.repository;

import com.prueba.gestion_restaurante_back.model.reservation.Reservation;
import com.prueba.gestion_restaurante_back.model.reservation.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByStatus(ReservationStatus status);

    @Query(value = "SELECT * FROM reservations r WHERE r.status IN ('PENDIENTE', 'CONFIRMADA') AND r.table_id = :tableId " +
            "AND r.reservation_date < :endTime " +
            "AND (r.reservation_date + (r.duration_hours || ' hours')::interval) > :startTime",
            nativeQuery = true)
    List<Reservation> findConflictingReservations(@Param("tableId") Long tableId,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);

    @Query("SELECT r FROM Reservation r WHERE r.status = 'PENDING' " +
            "AND r.reservationDate <= :cutoffTime")
    List<Reservation> findPendingReservationsBeforeCutoff(@Param("cutoffTime") LocalDateTime cutoffTime);

    @Query("SELECT r FROM Reservation r WHERE r.status IN ('PENDIENTE', 'CONFIRMADA')")
    List<Reservation> findActiveReservations();

}
