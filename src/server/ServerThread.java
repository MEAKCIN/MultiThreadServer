package server;

import protocol.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;

public class ServerThread extends Thread {
    protected BufferedReader inputStream;
    protected PrintWriter outputStream;
    protected Socket socket;
    private String line = new String();
    private String lines = new String();
    private static final int SOCKET_TIMEOUT= 30000;
    public ServerThread(Socket socket) {
        this.socket = socket;
        try{
            this.socket.setSoTimeout(SOCKET_TIMEOUT);
        }catch (IOException e){
            System.err.println("Error socket timeout: "+ e.getMessage());
        }
    }

    public void run() {
        try {
            this.inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.outputStream = new PrintWriter(this.socket.getOutputStream());

            while((this.line=this.inputStream.readLine())!=null){
                if (this.line.compareTo("ALPHA416 QUIT")==0){
                    System.out.println("Client requested to quit");
                    break;

                }
                String clientMessage = this.line;
                Parser parser= new Parser();
                HashMap<String,String> parsed_message= parser.clientMessageParse(clientMessage);
                boolean validquery=validQuery(parsed_message);
                if (validquery){
                    this.lines = "Client messaged : " + clientMessage + " at  : " + Thread.currentThread().getId();
                    this.outputStream.println(this.lines);
                    this.outputStream.flush();

                    PrintStream server_terminal_out = System.out;
                    String adress_string = String.valueOf(this.socket.getRemoteSocketAddress());
                    server_terminal_out.println("Client " + adress_string + " sent :  " + this.lines);

                }
                else {
                    String invalidRequest="Alpha416 ALPHA_400 Invalid Request\nError:Invalid parameter";
                    this.lines=invalidRequest;
                    this.outputStream.printf(invalidRequest);
                    this.outputStream.flush();
                    System.out.println("Client "+String.valueOf(this.socket.getRemoteSocketAddress())+" sent :  " + this.lines);
                }


            }
        } catch(SocketTimeoutException var){
            System.err.println("Socket timed out: "+ var.getMessage());
            this.outputStream.println("Time out");

        } catch (IOException var12) {
            this.line = this.getName();
            System.err.println("Server Thread. Run. IO Error/ Client " + this.line + " terminated abruptly");
        } catch (NullPointerException var13) {
            this.line = this.getName();
            System.err.println("Server Thread. Run.Client " + this.line + " Closed");
        } finally {
            try {
                System.out.println("Closing the connection");
                if (this.inputStream != null) {
                    this.inputStream.close();
                    System.err.println(" Socket Input Stream Closed");
                }

                if (this.outputStream != null) {
                    this.outputStream.close();
                    System.err.println("Socket Out Closed");

                }

                if (this.socket != null) {
                    this.socket.close();
                    System.err.println("Socket Closed");
                }
            } catch (IOException var11) {
                System.err.println("Socket Close Error");
            }

        }

    }
    public boolean validQuery(HashMap<String,String> message){ //this method helps to understand query is correct or not
        //decide exc or gas
        if(message.containsKey("-from")) {//if it includes from it is exchange
            if((message.get("-from")!=null || message.get("-from_name")!=null) && (message.get("-to")!=null || message.get("-to_name")!=null)){
                return true;

            }
            else{
                return false;
            }
        } else if (message.containsKey("date")) {
            return true;

        }
       else{
           return false;
        }
    }
}
