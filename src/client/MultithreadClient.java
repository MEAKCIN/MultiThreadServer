package client;
//todo write client using ConnectiontoServer and connect to Server
//todo check input of user if there is a

import java.io.IOException;
import java.util.Scanner;

public class MultithreadClient
{
    public static void main(String[] args){
        ConnectiontoServer connectiontoServer=new ConnectiontoServer(ConnectiontoServer.Default_Server_Adress,ConnectiontoServer.Default_Server_Port);
        Scanner scanner=new Scanner(System.in);
        try{
            connectiontoServer.connect();
            System.out.println("Server Client connection Completed\n Welcome to Client");
            System.out.println("Enter a message for sending Server");
            String message=scanner.nextLine();
            while(!message.equals("ALPHA416 QUIT")){
                System.out.println("Response from Server: "+ connectiontoServer.sendForAnswers(message));
                message=scanner.nextLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try{
                connectiontoServer.disconnect();
                scanner.close();
            } catch (IOException e ){
                System.err.println(e.getMessage());
            }
        }



    }
}
