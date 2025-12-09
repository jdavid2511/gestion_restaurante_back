package com.prueba.gestion_restaurante_back.repository;

import com.prueba.gestion_restaurante_back.model.table.RestaurantTable;
import com.prueba.gestion_restaurante_back.model.table.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    List<RestaurantTable> findByStatus(TableStatus status);

    @Query("SELECT r FROM RestaurantTable r WHERE r.capacity > :capacity AND r.status != 'CONFIRMADA'")
    List<RestaurantTable> findAvalibleTable(Integer capacity);

    boolean existsByTableNumber(Integer tableNumber);
}
