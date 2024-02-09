package com.ferme.itservices.api.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Data
@JsonIgnoreProperties(
    value = {"id", "createdAt", "updateAt"},
    allowGetters = true
)
public abstract class BaseEntity implements Serializable {
    @Id
    @JsonProperty("_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, unique = true, nullable = false)
    private UUID id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    @Column(nullable = false)
    private Date updatedAt;

    @PrePersist
    private void onCreate() {
        this.setCreatedAt(Date.from(Instant.now()));
        this.setUpdatedAt(Date.from(Instant.now()));
    }

    @PreUpdate
    private void onUpdate() {
        this.setUpdatedAt(Date.from(Instant.now()));
    }
}