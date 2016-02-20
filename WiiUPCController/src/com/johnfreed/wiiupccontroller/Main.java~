package com.johnfreed.wiiupccontroller;

public class Main {

	public static void main(String[] args) {
		try {
			// Set up the controller
			PCController controller = new PCController();
			
			// Set up the server
			WiiUServer server = new WiiUServer(controller);
			server.setPort(1337);
			server.setPrintDebugStatements(true);
			
			// Start the server
			server.StartServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
