package com.empresa.tienda.infrastructure.persistency.repository;

import com.empresa.tienda.infrastructure.persistency.entity.PrendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPrendaRepository extends JpaRepository<PrendaEntity, Long> {

    @Modifying
    @Query("UPDATE PrendaEntity p SET p.cantidad = :cantidad WHERE p.id = :id")
    void updateStock(@Param("id") Long id, @Param("cantidad") Integer cantidad);
}