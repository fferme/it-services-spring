package com.fermesolutions.itservices.model.enums;

public enum OrderItemType {
    LABOR(1),
    PART_BUYOUT(2),
    TRANSPORT(3);

    private int code;

    private OrderItemType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static OrderItemType valueOf(int code) {
        for (OrderItemType value: OrderItemType.values()){
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid OrderItemType code");
    }
}
