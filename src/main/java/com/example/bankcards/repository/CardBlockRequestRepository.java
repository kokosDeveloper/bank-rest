package com.example.bankcards.repository;

import com.example.bankcards.entity.CardBlockRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardBlockRequestRepository extends JpaRepository<CardBlockRequest, Long> {
    Page<CardBlockRequest> findByProcessedFalse(Pageable pageable);
    List<CardBlockRequest> findByUserId(Long userId);
}
