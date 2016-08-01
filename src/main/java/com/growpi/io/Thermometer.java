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
        return this.readCurrentTemperatures().getInsideTemperature();
    }

    public double readOutsideTemperature() {
        return this.readCurrentTemperatures().getOutsideTemperature();
    }

}

