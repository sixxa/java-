package com.sixa.cqrsbankingapp.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Getter
@Setter
public class ClientDto {

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
            message = "Name must be not NULL"
    )
    @Length(
            min=1,
            max=255,
            message = "name length must be in {min} and {max}"
    )
    private String name;
    @Email(
            message = "Username should be valid email"
    )
    @NotNull(
            message = "Username must be not NULL"
    )
    @Length(
            min=1,
            max=255,
            message = "Username length must be in {min} and {max}"
    )
    private String username;
    @NotNull(
            message = "Password must be not NULL"
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
