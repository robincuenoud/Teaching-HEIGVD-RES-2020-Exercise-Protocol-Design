//package ch.heigvd.res.examples;

import java.util.logging.*;
import java.io.*;
import java.net.Socket;

public class clientCalculator{

    final static int BUFF_SIZE = 1024;
    static final Logger LOG = Logger.getLogger("clientCalculator");

    public void sendRequest(){

        BufferedReader input = null;
        PrintWriter output = null;
        Socket clientS = null;
        boolean run = true;

        try {
            clientS = new Socket("localhost", 2022);

            input = new BufferedReader(new InputStreamReader(clientS.getInputStream()));
            output = new PrintWriter(clientS.getOutputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (run) {

                String inputUser = reader.readLine();
                if (inputUser.equals("exit")){
                    run = false;
                }

                output.println(inputUser);
                output.flush();

                LOG.log(Level.INFO, String.format("The server response: %s", input.readLine() ));
            }

        } catch(IOException e){
                LOG.log(Level.SEVERE, null, e);
            }

            finally { //end correctly the connection in all case

                try { //close input
                    input.close();
                }

                catch (IOException e){
                    Logger.getLogger("clientCalculator").log(Level.SEVERE, null, e);
                }
                // close output
                output.close();

                try{ // close socket
                    clientS.close();

                }

                catch (IOException e){

                    Logger.getLogger("clientCalculator").log(Level.SEVERE, null, e);
                }
            }


    }



    public static void main(String[] args){

        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s %n");
        clientCalculator client = new clientCalculator();

        client.sendRequest();
      
    }
}