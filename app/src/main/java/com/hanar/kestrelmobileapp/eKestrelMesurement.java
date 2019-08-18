package com.hanar.kestrelmobileapp;

enum eKestrelMeasurement {
    WindSpeed(0),
    Temperature(1),
    WindChill(2),
    Humidity(3),
    DiscomfortIndex(4);


    public final int valueId;
    private static eKestrelMeasurement[] vals = values();

    public eKestrelMeasurement next() {
        // No bounds checking required here, because the last instance overrides
            return vals[(this.ordinal()+1) % vals.length];
    }
    public eKestrelMeasurement previous() {
        // No bounds checking required here, because the last instance overrides
        return vals[ordinal() > 0 ? ordinal()  - 1 : 4];
    }

    private eKestrelMeasurement(int id) {
        this.valueId = id;
    }

}
