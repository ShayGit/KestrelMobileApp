package com.hanar.kestrelmobileapp;

enum eKestrelMeasurement {
    Temperature(0),
    WindSpeed(1),
    Humidity(2),
    WindChill(3),
    DiscomfortIndex(4);


    public final int valueId;

    private eKestrelMeasurement(int id) {
        this.valueId = id;
    }

}
