

public class GrowPiMain {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Please specify if GrowPi should run as a server or as a client.");
		}
		
		String role = args[0];
		switch (role) {
			case "server":
				startServer();
				break;
			case "client":
			default:
				startClient();
				break;
		}
		
		System.out.println("Done!");
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
