package com.sixa.cqrsbankingapp.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TransactionDto {

    @NotNull
            (
                    message = "Id must be not NUll",
                    groups = OnUpdate.class
            )
    @Null
            (
                    message = "Id must be NUll",
                    groups = OnCreate.class
            )
    private UUID id;

    @NotNull
            (
                    message = "Sender card be not NUll",
                    groups = OnCreate.class
            )
    @Null
            (
                    message = "Sender card must be NUll"
            )
    @Valid
    private CardDto from;
    @NotNull
            (
                    message = "Sender card be not NUll",
                    groups = OnCreate.class
            )
    @Null
            (
                    message = "Sender card must be NUll"
            )
    @Valid
    private CardDto to;

    @NotNull(
            message = "Amount must be not NULL"
    )
    @Positive(
            message = "Amount must be positive"
    )
    private BigDecimal amount;
}
