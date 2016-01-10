package cli;

import io.IniReader;

import java.io.IOException;

public class CLI {

    public static void execute(String string, boolean useSudo) {
        String[] cmd = {};
        if (useSudo) {
            String rootPassword = IniReader.read("system", "root_password");
            cmd[cmd.length] = "/bin/bash";
            cmd[cmd.length] = "-c";
            cmd[cmd.length] = "echo \"" + rootPassword + "\" | sudo"; 
        }
        cmd[cmd.length] = string;

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
