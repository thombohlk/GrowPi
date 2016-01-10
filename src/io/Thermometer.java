package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Thermometer {
    public final int INSIDE_TEMPERATURE_LINE = 0;
    public final int OUTSIDE_TEMPERATURE_LINE = 1;

    /**
     * Returns a list of temperature readings.
     * 
     * @return ArrayList<Double>
     */
    public ArrayList<Double> readCurrentTemperatures() {
        ArrayList<Double> readings = new ArrayList<Double>();
        readings.add(readTemperature(INSIDE_TEMPERATURE_LINE));
        readings.add(readTemperature(OUTSIDE_TEMPERATURE_LINE));

        return readings;
    }

    public double readInsideTemperature() {
        return readTemperature(INSIDE_TEMPERATURE_LINE);
    }

    public double readOutsideTemperature() {
        return readTemperature(OUTSIDE_TEMPERATURE_LINE);
    }

    protected double readTemperature(int line) {
        String[] cmd = { "/bin/bash", "-c",
                "echo \"thomas@pi\"| sudo -S lib/TEMPer2/temper" };

        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            Process ps = pb.start();

            BufferedReader input = new BufferedReader(new InputStreamReader(
                    ps.getInputStream()));

            while (line != 0) {
                input.readLine();
                line--;
            }

            String sTemp = input.readLine();
            if (sTemp == null) {
                throw new IOException("Could not read temperature on line "
                        + line);
            }
            System.out.println("I measured a temperature of " + sTemp);

            input.close();
            ps.destroy();

            return Double.valueOf(sTemp);
        } catch (IOException e) {
            System.out.println("ERROR: could not read temperature");
            e.printStackTrace();
            System.exit(1);
        }
        return 0;
    }

}
