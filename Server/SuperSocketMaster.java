// SuperSocketMaster.java
// Client Server Threaded Socket Object
// This object can start as a server or a client
// In server mode a poert is opened and each connection is added onto
// a portconnections vector
// Created By: Cadawas
// Created On: June 6 2007
// 
// May 29 2008 - Modified code to drop clients without killing interface
//               When a client connects or disconnects, a message is triggered
//               CONN = Connected
//               DISC = Disconnected
// June 2 2008 - Changed System outputs so that messages are clearer.  Fixed Comments
// January 4 2011 - Added more code on server side to remove client from vector if outgoing message fails.
//                  Still buggy... I think it depends on the order of who connects.
// December 12 2011 - Fixed dropping of clients causing a null pointer exception - removeClient method updated
// December 14 2011 - Added two public properties strMyIP and strMyHostname so you can see who to connect to
//                    Also made the portconnections object public so if you are the server
//                    You can get the inividial client properties


import java.io.*;
import java.net.*;
import java.util.*;


public class SuperSocketMaster implements Runnable {
  public ServerSocket serverObject = null;
  public Socket socketObject = null;
  private String strLine = "";
  private BufferedReader inBuffer = null;
  private PrintWriter outBuffer = null;
  private String strServerIP = "";
  private String strInText = "";
  // Dec 14 2011 new address/port data for the current machine
  public  String strMyIP = "";
  public String strMyHostname = "";
  
  private int intSocket;
  private boolean blnConnected = false;
  private boolean blnServerMode = true;
  // Special stucture that is used to hold the SuperSocketListener of each connected clients.
  public Vector<SuperSocketListen> portconnections = new Vector<SuperSocketListen>();
  
  // Server Mode Constructor
  SuperSocketMaster(int intSckt) {
    blnServerMode = true;
    strServerIP = "";
    intSocket = intSckt;
  }
  
  // Client Mode Constructor
  SuperSocketMaster(String strIP, int intSckt) {
    blnServerMode = false;
    strServerIP = strIP;
    intSocket = intSckt;
  }
  
  public void run() {
    if (blnServerMode == true) {
      // Server mode connection routine
      System.out.println("Opening Port: " + intSocket);
      try {
        serverObject = new ServerSocket(intSocket); 
        strMyIP = InetAddress.getLocalHost().getHostAddress();
        strMyHostname = InetAddress.getLocalHost().getHostName();
        System.out.println("Success creating server. IP: "+strMyIP+" Hostname: "+strMyHostname);
      } catch (IOException e) {
        System.out.println("Could not listen on port: " + intSocket);
        System.exit(-1);
      }
      
      while (true) {
        System.out.println("Listening for client");
        try {
          socketObject = serverObject.accept();
          blnConnected = true;
        } catch (IOException e) {
          System.out.println("Accept failed: " + intSocket);
          System.exit(-1);
        }
        try {
          inBuffer = new BufferedReader(new InputStreamReader(socketObject.getInputStream()));
          outBuffer = new PrintWriter(socketObject.getOutputStream(), true);
          System.out.println("Server: got connection from " + socketObject.toString() + "\n");
          String strClientIP = socketObject.getInetAddress().getHostAddress();
          String strClientHostname = socketObject.getInetAddress().getHostName();
          
          // Creating a thread to hande the inividial client connection
          SuperSocketListen singleconnection = new SuperSocketListen(outBuffer, inBuffer, socketObject.toString(), this, strClientIP, strClientHostname);
          portconnections.addElement(singleconnection);
          System.out.println("Elements in vector after add: " + portconnections.size());
          Thread t1 = new Thread(singleconnection);
          t1.start();
        } catch (IOException e) {
          System.out.println("in or out failed");
          System.exit(-1);
        }
      }
    } else {
      // Client mode connection routine
      System.out.println("Connecting to server");
      try {
        socketObject = new Socket(strServerIP, intSocket);
        strMyIP = InetAddress.getLocalHost().getHostAddress();
        strMyHostname = InetAddress.getLocalHost().getHostName();
        System.out.println("Success creating client. IP: "+strMyIP+" Hostname: "+strMyHostname);      
        outBuffer = new PrintWriter(socketObject.getOutputStream(), true);
        inBuffer = new BufferedReader(new InputStreamReader(socketObject.getInputStream()));            
        System.out.println("Connected and buffers initialized");
      } catch (UnknownHostException e) {
        System.out.println("Unknown host");
      } catch (IOException e) {
        System.out.println("No I/O");
      }
      // Listening to messages from the server
      // No child thread needed.  Clients only need one connection thread
      // and this one is it.
      while (strLine != null) {
        try {
          strLine = inBuffer.readLine();
          if (strLine != null) {
            System.out.println("Recieving Message: " + strLine);
            strInText = strLine;
          }
        } catch (IOException e) {
          System.out.println("Read failed Closing Connection");
          inBuffer = null;
          outBuffer = null;
          socketObject = null;
          strLine = null;
          //System.exit(-1);
        }
      }
      
    }
    
  }
  
  public void sendText(String strText) {
    if (blnServerMode == true) {
      // Server mode sending text needs to send to all clients
      // It therefore goes through the vector
      // and uses each object's sendText method.
      int intCounter;
      //System.out.println("Sending message to all "+portconnections.size()+" clients: "+strText);
      for (intCounter = 0; intCounter < portconnections.size(); intCounter++) {
        portconnections.get(intCounter).sendText(strText);
      }
    } else {
      // Client mode is much easier.
      //System.out.println("Sending message: "+strText);
      outBuffer.println(strText);
    }
  }
  
  public void removeClient(SuperSocketListen sslclient){
    System.out.println("Removing Client"+sslclient.strConnection+": "+portconnections.remove(sslclient));
    System.out.println("Number of Clients after remove: " + portconnections.size());
  }
  
  // This object is used in conjunction with the Server mode
  // of the SuperSocketMaster.  This object gets created
  // evertime a client connects to the server
  // so that the server can keep track all all connected client's
  // io buffers.
  public class SuperSocketListen implements Runnable {
    private BufferedReader in;
    private PrintWriter out;
    public String strConnection;
   public String strClientIP = "";
   public String strClientHostname = "";
    
    
    private SuperSocketMaster socketMaster;
    
    public SuperSocketListen(PrintWriter outgoing, BufferedReader incoming, String strConn, SuperSocketMaster ssm, String strClientIP, String strClientHostname) {
      out = outgoing;
      in = incoming;
      strConnection = strConn;
      socketMaster = ssm;
      this.strClientIP = strClientIP;
      this.strClientHostname = strClientHostname;
    }
    
    public void run() {
      String strLine = "";
      while (strLine != null) {
        try {
          strLine = in.readLine();
          if (strLine != null) {
            System.out.println("Recieving Client "+strClientIP+" Message: " + strLine);
          }
        } catch (IOException e) {
          // If the client disconnects, you will get an error.  Disconnect
          //  BTW, the while loop runs while there is data.  Thus the while will end and the thread should end as well.
          System.out.println("Client Read failed Closing Connection");
          in = null;
          out = null;
          strLine = null;
          socketMaster.removeClient(this);
        }
      }
    } 
    
    public void sendText(String strText) {
      
      if(out.checkError()){
        System.out.println("Client Write failed Closing Connection: "+strConnection);
        in = null;
        out = null;
        strLine = null;
        socketMaster.removeClient(this);        
      }else{
        out.println(strText);
        
      }
    }
  }
}