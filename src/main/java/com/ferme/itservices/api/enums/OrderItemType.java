package com.ferme.itservices.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderItemType {
    PART_BUYOUT("Compra de Peça"),
    PART_EXCHANGE("Troca de Peça"),
    MANPOWER("Mão de Obra"),
    CARRIAGE("Transporte");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}