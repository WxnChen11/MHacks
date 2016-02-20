package com.johnfreed.wiiupccontroller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

import com.johnfreed.wiiupccontroller.PCController.ButtonState;

public class WiiUServer {
	
	private int mPort = 1337;
	private boolean mPrintDebugStatements = false;
	private PCController mController;
	
	// Thanks for the info http://wiiubrew.org/wiki/Internet_Browser !
	private static final int A_BUTTON_MASK = 32768;
	private static final int B_BUTTON_MASK = 16384;
	private static final int X_BUTTON_MASK = 8192;
	private static final int Y_BUTTON_MASK = 4096;
	private static final int DPAD_LEFT_BUTTON_MASK = 2048;
	private static final int DPAD_RIGHT_BUTTON_MASK = 1024;
	private static final int DPAD_UP_BUTTON_MASK = 512;
	private static final int DPAD_DOWN_BUTTON_MASK = 256;
	private static final int ZL_BUTTON_MASK = 128;
	private static final int ZR_BUTTON_MASK = 64;
	private static final int L_BUTTON_MASK = 32;
	private static final int R_BUTTON_MASK = 16;
	private static final int PLUS_BUTTON_MASK = 8;
	private static final int MINUS_BUTTON_MASK = 4;
	private static final int HOME_BUTTON_MASK = 2;
	
	public WiiUServer(PCController controller) {
		this.mController = controller;
	}
	
	public void StartServer() throws IOException {
		ServerSocket serverSocket = new ServerSocket(mPort);
		System.out.println("Wii U PC Controller Server running on port " + mPort);
		
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				Scanner scanner = new Scanner(socket.getInputStream());
				
				String nextLine = scanner.nextLine();
				
				// Main page
				if (nextLine.split(" ")[1].equals("/")) {
					printDebugMessage("User connected");
					PrintWriter out = new PrintWriter(socket.getOutputStream());
					out.println("HTTP/1.1 200 OK");
					out.println("Content-Type: text/html");
					out.println();
					out.println(RetrieveIndexHTML());
					out.flush();
					out.close();
					scanner.close();
					continue;
				}
				
				// Favicon
				if (nextLine.split(" ")[1].equals("/favicon.ico")) {
					printDebugMessage("Requested favicon");
					PrintWriter out = new PrintWriter(socket.getOutputStream());
					out.println("HTTP/1.1 404 Not Found");
					out.println();
					out.flush();
					out.close();
					scanner.close();
					continue;
				}
				
				// Commands
				/*
				if (nextLine.split(" ")[0].equals("POST") && nextLine.split(" ")[1].equals("/Command"))
				{
					//System.out.println("Derp");
					//continue;
				}
				*/
				
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					
					if (line.equals("")) {
						line = scanner.nextLine();
						
						HashMap<String, Float> data = ParseWiiUData(line);
						
						// First we'll take a look at the hold variable
						String holdDataString = data.get("hold").toString().replace(".0", "");
						try {
							// Right Stick
							float XDeflection = data.get("rStickX");
							float YDeflection = data.get("rStickY");
							
							mController.MoveMouse(XDeflection, YDeflection);
							
							long holdData = Long.valueOf(holdDataString);
							
							// A button held
							if ((holdData & A_BUTTON_MASK) == A_BUTTON_MASK) {
								
							}
							
							// B button held
							if ((holdData & B_BUTTON_MASK) == B_BUTTON_MASK) {
								
							}
							
							// X button held
							if ((holdData & X_BUTTON_MASK) == X_BUTTON_MASK) {
								
							}
							
							// Y button held
							if ((holdData & Y_BUTTON_MASK) == Y_BUTTON_MASK) {
								
							}
							
							// ZL button held - Left Click
							if ((holdData & ZL_BUTTON_MASK) == ZL_BUTTON_MASK) {
								mController.LeftClickDown();
							}
							else {
								mController.LeftClickUp();
							}
							
							// ZR button held - Right Click
							if ((holdData & ZR_BUTTON_MASK) == ZR_BUTTON_MASK) {
								mController.RightClickDown();
							}
							else {
								mController.RightClickUp();
							}
						}
						catch (Exception ex) {
							
						}
					}
				}
				
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				out.println("HTTP/1.1 200 OK");
				out.println("Connection: close");
				out.println();
				out.flush();
				out.close();
				scanner.close();
			}
			catch (Exception ex) {
				printDebugMessage("Caught exception: " + ex.getMessage());
			}
		}
	}
	
	private HashMap<String, Float> ParseWiiUData(String JSON) {
		HashMap<String, Float> data = new HashMap<String, Float>();
		
		// Replace brackets
		String jsonData = JSON.replace("{", "");
		jsonData = jsonData.replace("}", "");
		
		// Example data coming in: {"a":0,"b":1,"c":2}
		
		String[] dataPairs = jsonData.split(",");
		
		for (int idx = 0; idx < dataPairs.length; idx++) {
			// Get a single data pair ex: "a":0
			String dataPair = dataPairs[idx];
			String[] temp = dataPair.split(":");
			
			// Get the key and value
			String key = temp[0].replace("\"", "");
			Float value = Float.valueOf(temp[1]);
			
			// Put the key value pair in the HashMap
			data.put(key, value);
		}
		
		return data;
	}
	
	private String RetrieveIndexHTML() throws IOException {
		String stringToReturn = "";
		
		String file = "resources/index.html";
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String line;
		while ((line = br.readLine()) != null)
		{
			stringToReturn += line + '\r';
		}
		
		return stringToReturn;
	}
	
	private void printDebugMessage(Object message) {
		if (mPrintDebugStatements)
		{
			System.out.println(message);
		}
	}
	
	// Getters and setters
	public int getPort() {
		return mPort;
	}

	public void setPort(int port) {
		this.mPort = port;
	}
	
	public boolean getPrintDebugStatements() {
		return mPrintDebugStatements;
	}
	
	public void setPrintDebugStatements(boolean printDebugStatements) {
		this.mPrintDebugStatements = printDebugStatements;
	}
}
