package com.fermesolutions.itservices.model.enums;

public enum OSType {
    WINDOWS_7_STARTER(1),
    WINDOWS_7_HOME_BASIC(2),
    WINDOWS_7_HOME_PREMIUM(3),
    WINDOWS_7_ENTERPRISE(4),
    WINDOWS_7_ULTIMATE(4),
    WINDOWS_8(5),
    WINDOWS_8_PRO(6),
    WINDOWS_8_ENTERPRISE(7),
    WINDOWS_10_HOME(8),
    WINDOWS_10_PRO(9),
    WINDOWS_10_PRO_FOR_WORKSTATIONS(10),
    WINDOWS_10_EDUCATION(11),
    WINDOWS_10_PRO_EDUCATION(12),
    WINDOWS_10_ENTERPRISE(13),
    WINDOWS_11_HOME(14),
    WINDOWS_11_PRO(15);

    private final int code;

    private OSType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static OSType valueOf(int code) {
        for (OSType value: OSType.values()){
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid ActionType code");
    }
}
