package com.ferme.itservices.security.auditing.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Embeddable
public class AuditInfo implements Serializable {
	@CreatedDate
	@Column(nullable = false, updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", locale = "pt_BR")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(insertable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", locale = "pt_BR")
	private LocalDateTime updatedAt;

	@CreatedBy
	@Column(nullable = false, updatable = false)
	private String createdBy;

	@LastModifiedBy
	@Column(insertable = false)
	private String updatedBy;
}

