package com.growpi.cli;

import com.growpi.io.IniReader;

import java.io.IOException;

public class CLI {

    public static void execute(String[] cmd) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < cmd.length; i++) {
               strBuilder.append(cmd[i] + " ");
        }
        System.out.println("Executing command: " + strBuilder.toString());
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            Process ps = pb.start();
            ps.waitFor();
            ps.destroy();
        } catch (IOException | InterruptedException e) {
            System.out.println("ERROR: could not instruct boinc");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
