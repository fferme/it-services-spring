package com.fermesolutions.itservices.model.enums;

public enum ComputerType {
    DESKTOP(1),
    NOTEBOOK(2);

    private int code;

    private ComputerType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ComputerType valueOf(int code) {
        for (ComputerType value: ComputerType.values()){
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid ComputerType code");
    }
}
