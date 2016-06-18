package com.growpi.io;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;

public class IniReader {
    
    public static String read(String node, String key) {
        Ini ini = null;
        try {
            ini = new Ini(new File("ini/general.ini"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Preferences prefs = new IniPreferences(ini);
        return prefs.node(node).get(key, null);
    }

}
