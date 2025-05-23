package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CardBlockRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
    @Column(nullable = false)
    private String reason;
    @Column(nullable = false)
    private boolean processed = false;
    @Column(nullable = false)
    private boolean approved = false;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime requestedAt;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime processedAt;
    @LastModifiedBy
    @Column(insertable = false)
    private Long lastModifiedBy;
}
