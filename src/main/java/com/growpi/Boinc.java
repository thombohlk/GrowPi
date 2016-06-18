package com.growpi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import com.growpi.cli.CLI;
import com.growpi.io.BoincPreferenceEditor;
import com.growpi.io.IniReader;

public class Boinc {

    protected static final String SERVICE_COMMAND_START = "always";
    protected static final String SERVICE_COMMAND_STOP = "never";

    protected InfluxDB db;

    public Boinc() {
        db = InfluxDBFactory.connect(IniReader.read("influxdb", "ip"),
                IniReader.read("influxdb", "username"),
                IniReader.read("influxdb", "password"));
    }

    public void start(double percentage) {
        instructService(SERVICE_COMMAND_START);
        BoincPreferenceEditor.insertOrUpdate("PREF_NAME_MAX_CPU", Double.toString(percentage * 100));
        logWorkload(percentage);
    }

    public void stop() {
        instructService(SERVICE_COMMAND_STOP);
        logWorkload(0);
    }

    protected void logWorkload(double percentage) {
        String hostname = "foo";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Point point = Point.measurement("workers")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .field(hostname, percentage).build();

        db.write(IniReader.read("influxdb", "dbname"), "default", point);
    }

    protected void instructService(String instruction) {
        String password = IniReader.read("boinc", "password");
        String[] command = {"boinccmd", "--passwd", password, "--set_run_mode", instruction};
        CLI.execute(command);
    }

}
