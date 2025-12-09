package com.prueba.gestion_restaurante_back.service;

import com.prueba.gestion_restaurante_back.dto.CustomerDTO;
import com.prueba.gestion_restaurante_back.dto.ReservationDTO;
import com.prueba.gestion_restaurante_back.dto.TableDTO;
import com.prueba.gestion_restaurante_back.model.customer.Customer;
import com.prueba.gestion_restaurante_back.model.reservation.Reservation;
import com.prueba.gestion_restaurante_back.model.reservation.ReservationStatus;
import com.prueba.gestion_restaurante_back.model.restaurant_config.RestaurantConfig;
import com.prueba.gestion_restaurante_back.model.table.RestaurantTable;
import com.prueba.gestion_restaurante_back.model.table.TableStatus;
import com.prueba.gestion_restaurante_back.model.waitlist.WaitList;
import com.prueba.gestion_restaurante_back.repository.ReservationRepository;
import com.prueba.gestion_restaurante_back.repository.RestaurantConfigRepository;
import com.prueba.gestion_restaurante_back.repository.RestaurantTableRepository;
import com.prueba.gestion_restaurante_back.repository.WaitlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private final CustomerService customerService;
    private final RestaurantConfigRepository restaurantConfigRepository;
    private final WaitlistRepository waitlistRepository;

    @Transactional
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        //validar fecha futura
        if (reservationDTO.getReservationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La reserva no puede ser de un fecha anterior");
        }

        //validar horario actual
        validateOperatingHours(reservationDTO.getReservationDate());

        //Buscar o crear cliente
        Customer customer = convertToEntity(reservationDTO.getCustomerDTO());

        //Bucar mesa
        RestaurantTable table = restaurantTableRepository.findById(reservationDTO.getTableId())
                .orElseThrow(() -> new RuntimeException("La mesa no existe"));

        //validar capacidad
        if (table.getCapacity() < reservationDTO.getNumberOfPeople()) {
            throw new RuntimeException("El numero de personas supera la capacidad de la mesa");
        }

        //verificar si esta disponible
        LocalDateTime endTime = reservationDTO.getReservationDate()
                .plusMinutes((long)(reservationDTO.getDurationHours() * 60));

        List<Reservation> conflicts = reservationRepository.findConflictingReservations(table.getId(), reservationDTO.getReservationDate(), endTime);

        if (!conflicts.isEmpty()) {
            if (customer.getIsCustomerVip()) {
                return addToWaitlist(customer, reservationDTO);
            }
            throw new RuntimeException("La mesa no esta disponible para la fecha requerida");
        }

        //crear reserva
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setTable(table);
        reservation.setReservationDate(reservationDTO.getReservationDate());
        reservation.setNumberOfPeople(reservationDTO.getNumberOfPeople());
        reservation.setDurationHours(reservationDTO.getDurationHours());
        reservation.setStatus(ReservationStatus.PENDIENTE);
        reservation.setCreatedAt(LocalDateTime.now());

        Reservation savedReservation = reservationRepository.save(reservation);

        //Actualizar Reserva
        table.setStatus(TableStatus.RESERVADO);
        restaurantTableRepository.save(table);

        return convertToDTO(savedReservation);
    }

    @Transactional
    public ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO) {

        //validar fecha futura
        if (reservationDTO.getReservationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La reserva no puede ser de un fecha anterior");
        }

        //validar horario actual
        validateOperatingHours(reservationDTO.getReservationDate());


        //Buscar o crear cliente
        Customer customer = convertToEntity(reservationDTO.getCustomerDTO());

        //Bucar mesa
        RestaurantTable table = restaurantTableRepository.findById(reservationDTO.getTableId())
                .orElseThrow(() -> new RuntimeException("La mesa no existe"));

        //validar capacidad
        if (table.getCapacity() < reservationDTO.getNumberOfPeople()) {
            throw new RuntimeException("El numero de personas supera la capacidad de la mesa");
        }

        //verificar si esta disponible
        LocalDateTime endTime = reservationDTO.getReservationDate()
                .plusMinutes((long)(reservationDTO.getDurationHours() * 60));

        List<Reservation> conflicts = reservationRepository.findConflictingReservations(table.getId(), reservationDTO.getReservationDate(), endTime);

        if (!conflicts.isEmpty()) {
            if (customer.getIsCustomerVip()) {
                return addToWaitlist(customer, reservationDTO);
            }
            throw new RuntimeException("La mesa no esta disponible para la fecha requerida");
        }

        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("La reserva no existe"));
        //actualizar reserva
        reservation.setCustomer(customer);
        reservation.setTable(table);
        reservation.setReservationDate(reservationDTO.getReservationDate());
        reservation.setNumberOfPeople(reservationDTO.getNumberOfPeople());
        reservation.setDurationHours(reservationDTO.getDurationHours());

        Reservation savedReservation = reservationRepository.save(reservation);

        //Actualizar Reserva
        table.setStatus(TableStatus.RESERVADO);
        restaurantTableRepository.save(table);

        return convertToDTO(savedReservation);
    }

    @Transactional
    public ReservationDTO confirmReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("La reserva no existe"));

        if (reservation.getStatus() !=  ReservationStatus.PENDIENTE) {
            throw new RuntimeException("La reserva no puede ser finalizado");
        }

        reservation.setStatus(ReservationStatus.CONFIRMADA);
        reservation.setConfirmedAt(LocalDateTime.now());

        //si es cliente VIP agregar los puntos
        Customer customer = reservation.getCustomer();
        if (customer.getIsCustomerVip()) {
            int points = customer.getRangeLevel().getPointsPerReservation();
            customer.addPoints(points);
        }

        Reservation updateReservation = reservationRepository.save(reservation);
        return convertToDTO(updateReservation);
    }

    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("La reserva no existe"));

        reservation.setStatus(ReservationStatus.CANCELADA);
        reservationRepository.save(reservation);

        //cambiar el estado de la mesa
        RestaurantTable table = reservation.getTable();
        table.setStatus(TableStatus.DISPONIBLE);
        restaurantTableRepository.save(table);
    }

    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ReservationDTO> getActiveReservations() {
        return reservationRepository.findActiveReservations().stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public void validateOperatingHours(LocalDateTime dateTime) {
        RestaurantConfig restaurantConfig = restaurantConfigRepository.findAll().stream().findFirst().orElse(new RestaurantConfig(1L, LocalTime.of(10, 0), LocalTime.of(22, 00), 0.20));

        LocalTime time = dateTime.toLocalTime();
        if (time.isBefore(restaurantConfig.getOpenTime()) || time.isAfter(restaurantConfig.getCloseTime())) {
            throw new RuntimeException(String.format("La reserva solo esta permitida en el horario entre %s y %S",
                    restaurantConfig.getOpenTime(), restaurantConfig.getCloseTime()));
        }
    }

    private ReservationDTO addToWaitlist(Customer customer, ReservationDTO reservationDTO) {
        long totalTables = restaurantTableRepository.count();
        long currentWaitlist = waitlistRepository.count();
        double maxAllowed = totalTables * 0.20;

        if (currentWaitlist >= maxAllowed) {
            throw new RuntimeException("La lista de espera esta llena");
        }

        WaitList waitList = new WaitList();
        waitList.setCustomer(customer);
        waitList.setRequestDate(reservationDTO.getReservationDate());
        waitList.setNumberOfPeople(reservationDTO.getNumberOfPeople());
        waitList.setPriority(customer.getPoints());
        waitList.setCreatedAt(LocalDateTime.now());

        waitlistRepository.save(waitList);

        throw new RuntimeException("No hay mesas disponibles, fué agregado la lista VIP del restaurante");
    }

    public Customer convertToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setId(customerDTO.getId());
        customer.setNit(customerDTO.getNit());
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setRangeLevel(customerDTO.getRangeLevel());
        customer.setPoints(customerDTO.getPoints());
        customer.setPoints(customerDTO.getPoints());

        return customer;
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(reservation.getId());
        reservationDTO.setCustomerDTO(customerService.convertToDTO(reservation.getCustomer()));
        reservationDTO.setTableId(reservation.getTable().getId());
        reservationDTO.setReservationDate(reservation.getReservationDate());
        reservationDTO.setNumberOfPeople(reservation.getNumberOfPeople());
        reservationDTO.setDurationHours(reservation.getDurationHours());
        reservationDTO.setStatus(reservation.getStatus());
        reservationDTO.setCreatedAt(reservation.getCreatedAt());
        reservationDTO.setConfirmedAt(reservation.getConfirmedAt());
        return reservationDTO;
    }
}
