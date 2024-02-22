package com.ferme.itservices.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;


@SuperBuilder
@Data
@NoArgsConstructor
@JsonIgnoreProperties(
    value = {"id", "createdAt", "updateAt"},
    allowGetters = true
)
public class BaseEntityDTO {
    @JsonProperty("_id")
    public UUID id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    public Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    public Date updatedAt;
}