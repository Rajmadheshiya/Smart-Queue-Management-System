package com.raj.smartqueue.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raj.smartqueue.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByNameContainingIgnoreCase(String name);
    Optional<Patient> findByTokenNumber(Integer tokenNumber);
    boolean existsByPhoneAndStatus(String phone, String status);

    @Query("SELECT COUNT(p) FROM Patient p WHERE p.status = 'Waiting'")
    long getWaitingCount();

    @Query("SELECT COUNT(p) FROM Patient p WHERE p.status = 'Completed'")
    long getCompletedCount();

}