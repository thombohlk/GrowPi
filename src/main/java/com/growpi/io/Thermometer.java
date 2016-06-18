package com.growpi.io;

import com.eden314.jtemper.Temper2Reader;
import com.eden314.jtemper.TemperatureObject;


public class Thermometer {

    /**
     * Returns a list of temperature readings.
     * 
     * @return TemperatureObject
     */
    public TemperatureObject readCurrentTemperatures() {
        Temper2Reader reader = new Temper2Reader();
        return reader.getTemperature();
    }

    public double readInsideTemperature() {
        TemperatureObject temperatures = this.readCurrentTemperatures();
        return temperatures.getInsideTemperature();
    }

    public double readOutsideTemperature() {
        TemperatureObject temperatures = this.readCurrentTemperatures();
        return temperatures.getOutsideTemperature();
    }

}

