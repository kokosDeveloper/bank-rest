package com.example.bankcards.entity;

import com.example.bankcards.entity.converter.YearMonthDateAttributeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String number;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Convert(converter = YearMonthDateAttributeConverter.class)
    private YearMonth expirationDate;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private BigDecimal moneyAmount;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;
}
