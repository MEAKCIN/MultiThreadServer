package server;

import protocol.ClientParser;

import protocol.RespondParser;
import restfulAPI.RESTfulAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerThread extends Thread {
    protected BufferedReader inputStream;
    protected PrintWriter outputStream;
    protected Socket socket;
    private String line = new String();
    private String lines = new String();
    private static final int SOCKET_TIMEOUT= 100000;
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

            while((this.line=this.inputStream.readLine())!=null){//if quit it exits from server
                if (this.line.compareTo("ALPHA416 QUIT")==0){
                    System.out.println("Alpha416 ALPHA_200 Success\nClient requested to quit");
                    break;
                }


                String clientMessage = this.line;
                ClientParser parser= new ClientParser();
                HashMap<String,String> parsed_message= parser.clientMessageParse(clientMessage);
                boolean validquery=validQuery(parsed_message);
                RespondParser respond= new RespondParser();
                String response=new String();
                if (validquery){
                    this.lines = "Client messaged : " + clientMessage + " at  : " + Thread.currentThread().getId();
                    this.outputStream.println(this.lines);
                    this.outputStream.flush();
                    if(clientMessage.contains("GAS")){
                        RESTfulAPI restfulAPI = new RESTfulAPI();
                        try {
                            response= restfulAPI.gasData(parsed_message);
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }


                    }
                    else if (clientMessage.contains("EXC")){
                        RESTfulAPI restfulAPI = new RESTfulAPI();
                        response= restfulAPI.exchange(parsed_message);
                        System.out.println("done");

                    }

                    this.outputStream.printf(response);
                    this.outputStream.flush();



                    PrintStream server_terminal_out = System.out;
                    String adress_string = String.valueOf(this.socket.getRemoteSocketAddress());
                    server_terminal_out.println("Client " + adress_string + " sent :  " + this.lines);

                }
                else {
                    response=respond.exchangeInvalidRequest();
                    this.lines=response;
                    this.outputStream.printf(response);
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
        } catch (NullPointerException | URISyntaxException var13) {
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
            if((message.get("-from")!=null ) && (message.get("-to")!=null)){
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
