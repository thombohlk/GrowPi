import io.IniReader;
import io.Thermometer;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class GrowPiServer implements Runnable {

    protected Jedis jedis;
    protected Thermometer thermometer;

    public GrowPiServer() {
        thermometer = new Thermometer();

        try {
            jedis = new Jedis("192.168.0.16");
            jedis.connect();
        } catch (JedisConnectionException e) {
            System.out.println("Error: could not connect to Redis");
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void run() {
        while (true) {
            double outsideTemperature = thermometer.readOutsideTemperature();
            double insideTemperature = thermometer.readInsideTemperature();

            logTemperature("outside", outsideTemperature);
            logTemperature("inside", insideTemperature);

            if (outsideTemperature < 23.5) {
                jedis.publish("boinc", "start");
            } else if (outsideTemperature > 24) {
                jedis.publish("boinc", "stop");
            }

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                // no worries, carry on
            }
        }
    }

    protected void logTemperature(String field, double currentTemperature) {
        InfluxDB db = InfluxDBFactory.connect(
                IniReader.read("influxdb", "ip"),
                IniReader.read("influxdb", "username"),
                IniReader.read("influxdb", "password"));
        
        Point point = Point.measurement("temperature")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .field(field, currentTemperature)
                .build();
        
        db.write(IniReader.read("influxdb", "dbname"), "default", point);
    }

}
