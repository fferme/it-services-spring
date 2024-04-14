package com.ferme.itservices.security.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
    @JsonProperty(value = "Owner") OWNER("Owner"),
    @JsonProperty(value = "Admin") ADMIN("Admin"),
    @JsonProperty(value = "Guest") GUEST("Guest");

    private final String value;

    @Override
    public String toString() {
        return this.value;
    }
}