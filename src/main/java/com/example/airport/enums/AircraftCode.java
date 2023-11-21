package com.example.airport.enums;

public enum AircraftCode {
    B38M,//186
    B738,//165
    E75L,//82
    E190,//106
    E195,//112
    DH8D,//39
    A20N,//112
    A320//150
    ;

    public static boolean contains(String apiIcao) {
        for (AircraftCode code : values()) {
            if (code.name().equals(apiIcao)) {
                return true;
            }
        }
        return false;
    }
}
