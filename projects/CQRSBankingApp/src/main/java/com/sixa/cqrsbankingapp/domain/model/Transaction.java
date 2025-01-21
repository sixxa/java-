package com.sixa.cqrsbankingapp.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "transactions")
@Entity
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @ManyToOne
    private Card from;

    @ManyToOne
    private Card to;

    private BigDecimal amount;
}
