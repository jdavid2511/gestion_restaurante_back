package com.prueba.gestion_restaurante_back.repository;

import com.prueba.gestion_restaurante_back.model.waitlist.WaitList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitlistRepository extends JpaRepository<WaitList, Long> {
    long count();
}
