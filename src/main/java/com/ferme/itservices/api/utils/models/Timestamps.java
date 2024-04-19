package com.ferme.itservices.api.utils.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Embeddable
@AllArgsConstructor
@Setter
@Getter
public class Timestamps {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    @Column(nullable = false, updatable = false)
    protected Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    @Column(nullable = false)
    protected Date updatedAt;

	public Timestamps() {
		this.createdAt = Date.from(Instant.now());
		this.updatedAt = Date.from(Instant.now());
	}

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