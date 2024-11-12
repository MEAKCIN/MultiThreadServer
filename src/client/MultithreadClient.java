package client;//

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class MultithreadClient {
    public MultithreadClient() {
    }

    public static void main(String[] args) {
        ConnectionToServer connectionToServer = new ConnectionToServer("localhost", 4444);
        Scanner scanner = new Scanner(System.in);

        try {
            connectionToServer.connect();

            System.out.println("Enter a message for the echo");

            String message=scanner.nextLine();
            while (!message.equalsIgnoreCase("ALPHA416 QUIT")) {
                if (message=="Time out"){
                    connectionToServer.disconnect();
                    System.out.println("Disconnected cause of Time out");
                }
                PrintStream ClientResponse = System.out;
                connectionToServer.sendForAnswer(message);
                String serverResponse=new String("");

                while(serverResponse!=null){
                    serverResponse= connectionToServer.getForAnswer();
                    ClientResponse.printf("Response from server: " + serverResponse+"\n");
                }

                message = scanner.nextLine();
            }




        } catch (IOException var12) {
            IOException e = var12;
            System.err.println(e.getMessage());
        } finally {
            try {
                connectionToServer.disconnect();
                scanner.close();
            } catch (IOException var11) {
                IOException e = var11;
                System.err.println(e.getMessage());
            }

        }

    }
}
