package com.ferme.itservices.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
@Entity(name = "clients")
@Table(name = "clients")
public class Client extends BaseEntity implements Serializable {
	@Setter
	@NotEmpty(message = "Name is required")
	@Size(min = 4, max = 40, message = "Name must be between 4 and 40 characters")
	@Column(length = 40, nullable = false)
	private String name;

	@Setter
	@NotEmpty(message = "Phone number is required")
	@Pattern(regexp = "^\\(?(\\d{2})\\)?[- ]?(\\d{4,5})[- ]?(\\d{4})$", message = "Invalid phone number format")
	@Size(min = 8, max = 11, message = "Phone number must be between 8 and 11 characters")
	@Column(name = "phone_number", length = 11, unique = true, nullable = false)
	private String phoneNumber;

	@Setter
	@Size(max = 20)
	@Column(length = 20)
	private String neighborhood;

	@Setter
	@Size(max = 50)
	@Column(length = 50)
	private String address;

	@JsonIgnore
	@OneToMany(mappedBy = "client", cascade = CascadeType.MERGE)
	private List<Order> orders;

	@Setter
	@Size(max = 70)
	@Column(length = 70)
	private String reference;

	@PrePersist
	protected void onPrePersist() {
		if (getCreatedOn() == null) { setCreatedOn(LocalDateTime.now()); }
		if (getUpdatedOn() == null) { setUpdatedOn(LocalDateTime.now()); }
	}

	@PreUpdate
	protected void onPreUpdate() {
		setUpdatedOn(LocalDateTime.now());
	}
}