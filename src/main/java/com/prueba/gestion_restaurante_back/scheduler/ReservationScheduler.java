package com.prueba.gestion_restaurante_back.scheduler;

import com.prueba.gestion_restaurante_back.model.reservation.Reservation;
import com.prueba.gestion_restaurante_back.model.reservation.ReservationStatus;
import com.prueba.gestion_restaurante_back.model.table.TableStatus;
import com.prueba.gestion_restaurante_back.repository.ReservationRepository;
import com.prueba.gestion_restaurante_back.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationScheduler {

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository  restaurantTableRepository;

    @Scheduled(cron = "0 */5 * * * ?")
    @Transactional
    public void automaticCancelReservations() {
        LocalDateTime cutoffTime = LocalDateTime.now().plusMinutes(30);

        List<Reservation> pendingReservations = reservationRepository.findPendingReservationsBeforeCutoff(cutoffTime);

        for (Reservation reservation : pendingReservations) {
            reservation.setStatus(ReservationStatus.CANCELADA);
            reservationRepository.save(reservation);

            //liberar mesa
            reservation.getTable().setStatus(TableStatus.DISPONIBLE);
            restaurantTableRepository.save(reservation.getTable());
        }

        if (!pendingReservations.isEmpty()) {
            log.info("{} reservas sin confirmar canceladas", pendingReservations.size());
        }
    }
}
