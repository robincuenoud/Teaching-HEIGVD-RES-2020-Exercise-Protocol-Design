package ch.heigvd.res.examples;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A very simple example of TCP server. When the server starts, it binds a
 * server socket on any of the available network interfaces and on port 2205. It
 * then waits until one (only one!) client makes a connection request. When the
 * client arrives, the server does not even check if the client sends data. It
 * simply writes the current time, every second, during 15 seconds.
 *
 * To test the server, simply open a terminal, do a "telnet localhost 2205" and
 * see what you get back. Use Wireshark to have a look at the transmitted TCP
 * segments.
 *
 * @author Olivier Liechti
 */
public class StreamingTimeServer {

  static final Logger LOG = Logger.getLogger(StreamingTimeServer.class.getName());

  private final int TEST_DURATION = 15000;
  private final int PAUSE_DURATION = 1000;

  private final int LISTEN_PORT = 2202;
  enum  Operation {
    PLUS,
    MINUS,
    TIMES,
    WRONG
  }
  /**
   * This method does the entire processing.
   */
  public void start() {
    LOG.info("Starting server...");

    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    BufferedReader reader = null;
    PrintWriter writer = null;

    try {
      LOG.log(Level.INFO, "Creating a server socket and binding it on any of the available network interfaces and on port {0}", new Object[]{Integer.toString(LISTEN_PORT)});
      serverSocket = new ServerSocket(LISTEN_PORT);
      logServerSocketAddress(serverSocket);


        LOG.log(Level.INFO, "Waiting (blocking) for a connection request on {0} : {1}", new Object[]{serverSocket.getInetAddress(), Integer.toString(serverSocket.getLocalPort())});
        clientSocket = serverSocket.accept();

        LOG.log(Level.INFO, "A client has arrived. We now have a client socket with following attributes:");
        logSocketAddress(clientSocket);

        LOG.log(Level.INFO, "Getting a Reader and a Writer connected to the client socket...");
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writer = new PrintWriter(clientSocket.getOutputStream());
        boolean correct = true;
      while(correct && clientSocket.isConnected()) {
        LOG.log(Level.INFO, "Starting my job... sending operation list to client", TEST_DURATION);
        writer.println("Les operation sont * + - , entrer sous forme OP1 OP2 Operation");
        writer.flush();
        String responseString = reader.readLine();
        if(responseString != null) {
          writer.println("server received "+responseString);
          writer.flush();
          String[] response =responseString.split(" ");
          try {
            Operation operation = getOperation(response[2]);
            if (operation == Operation.WRONG) {
              writer.println("Wrong operation");
              writer.flush();
              correct = false;
            }
            writer.println(calculate(Integer.parseInt(response[0]), Integer.parseInt(response[1]), operation));
            writer.flush();
          } catch (IndexOutOfBoundsException e) {
            writer.println("Wrong input");
            writer.flush();
            correct = false;
          }
        }
      }
        reader.close();
        writer.close();
        clientSocket.close();

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      LOG.log(Level.INFO, "We are done. Cleaning up resources, closing streams and sockets...");
      try {
        reader.close();
      } catch (IOException ex) {
        Logger.getLogger(StreamingTimeServer.class.getName()).log(Level.SEVERE, null, ex);
      }
      writer.close();
      try {
        clientSocket.close();
      } catch (IOException ex) {
        Logger.getLogger(StreamingTimeServer.class.getName()).log(Level.SEVERE, null, ex);
      }
      try {
        serverSocket.close();
      } catch (IOException ex) {
        Logger.getLogger(StreamingTimeServer.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

  }

  /**
   * A utility method to print server socket information
   *
   * @param serverSocket the socket that we want to log
   */
  private void logServerSocketAddress(ServerSocket serverSocket) {
    LOG.log(Level.INFO, "       Local IP address: {0}", new Object[]{serverSocket.getLocalSocketAddress()});
    LOG.log(Level.INFO, "             Local port: {0}", new Object[]{Integer.toString(serverSocket.getLocalPort())});
    LOG.log(Level.INFO, "               is bound: {0}", new Object[]{serverSocket.isBound()});
  }

  /**
   * A utility method to print socket information
   *
   * @param clientSocket the socket that we want to log
   */
  private void logSocketAddress(Socket clientSocket) {
    LOG.log(Level.INFO, "       Local IP address: {0}", new Object[]{clientSocket.getLocalAddress()});
    LOG.log(Level.INFO, "             Local port: {0}", new Object[]{Integer.toString(clientSocket.getLocalPort())});
    LOG.log(Level.INFO, "  Remote Socket address: {0}", new Object[]{clientSocket.getRemoteSocketAddress()});
    LOG.log(Level.INFO, "            Remote port: {0}", new Object[]{Integer.toString(clientSocket.getPort())});
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s %n");

    StreamingTimeServer server = new StreamingTimeServer();
    server.start();
  }


  private Operation getOperation(String input) {
  if(input.equals("+")) return Operation.PLUS;
  if(input.equals("-")) return Operation.MINUS;
  if(input.equals("*")) return Operation.TIMES;
  return Operation.WRONG;
  }
  private int calculate(int op1, int op2, Operation operation) {
    System.out.println(op1 + " " + op2);
  switch(operation) {
    case PLUS: return op1 + op2;
    case TIMES: return op1 * op2;
    case MINUS: return op1 - op2;
    default:
      return 0;
  }
  }
}

