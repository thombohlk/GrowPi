package com.growpi;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.apache.commons.cli.DefaultParser;

public class GrowPiMain {

	public static void main(String[] args) {
        CommandLine cmd = parseArguments(args);
        
        String role = cmd.getOptionValue("role");
		System.out.println(role);
        
        switch (role) {
			case "server":
				startServer();
				break;
			case "client":
			default:
				startClient();
				break;
		}
	}

    protected static CommandLine parseArguments(String[] args) {
        CommandLine result = null;
        
        try {
            Options options = new Options();
            options.addOption("r", "role", true, "whether Eden314 should run as client or server");

            CommandLineParser parser = new DefaultParser();
            result = parser.parse(options, args);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }

        return result;
    }

	protected static void startClient() {
		GrowPiClient client = new GrowPiClient();
		client.run();
	}

	protected static void startServer() {
		Thread t = new Thread(new GrowPiClient());
		t.start();
		(new GrowPiServer()).run();
	}

}
