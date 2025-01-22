package com.sixa.cqrsbankingapp.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CardDto {

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

    @NotNull(
            message = "Card number must be not NULL",
            groups = OnCreate.class
    )
    @Null(
            message = "Card number must be NULL"
    )
    private String number;
    @NotNull(
            message = "Card Date must be not NULL",
            groups = OnCreate.class
    )
    @Null(
            message = "Card date must be NULL"
    )
    private String Date;
    @NotNull(
            message = "Card cvv must be not NULL",
            groups = OnCreate.class
    )
    @Null(
            message = "Card cvv must be NULL"
    )
    private String cvv;

}
