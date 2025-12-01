package com.prueba.gestion_restaurante_back.repository;

import com.prueba.gestion_restaurante_back.model.restaurant_config.RestaurantConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantConfigRepository extends JpaRepository<RestaurantConfig, Long> {
}
