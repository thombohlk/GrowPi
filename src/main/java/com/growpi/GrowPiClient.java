package com.growpi;

import com.eden314.jboinc.Boinc;
import com.growpi.io.IniReader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class GrowPiClient extends JedisPubSub implements Runnable {

    protected Boinc boinc;

    public GrowPiClient() {
        boinc = new Boinc(IniReader.read("boinc", "password"));
    }

    @Override
    public void run() {
        Jedis jedis = new Jedis(IniReader.read("redis", "ip"));
        jedis.connect();
        jedis.subscribe(this, "boinc");
    }

    public void onMessage(String channel, String message) {
        switch (channel) {
        case "boinc":
            onBoincMessage(message);
            break;

        default:
            // no interest
            break;
        }
    }

    protected void onBoincMessage(String message) {
        switch (message) {
        case "start":
            boinc.start();
            break;
        case "stop":
            boinc.stop();
            break;
        default:
            // error
            System.out.println("Error: unknown message received: " + message);
            break;
        }
    }

}
