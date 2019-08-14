package com.hanar.kestrelmobileapp;

enum eKestrelMeasurement {
    Temperature(0),
    WindSpeed(1),
    Humidity(2),
    WindChill(3),
    DiscomfortIndex(4);


    public final int valueId;
    public eKestrelMeasurement next() {
        // No bounds checking required here, because the last instance overrides
        return values()[ordinal() + 1];
    }
    public eKestrelMeasurement previous() {
        // No bounds checking required here, because the last instance overrides
        return values()[ordinal() - 1];
    }

    private eKestrelMeasurement(int id) {
        this.valueId = id;
    }

}
