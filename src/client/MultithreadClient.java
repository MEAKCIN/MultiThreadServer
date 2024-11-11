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

            for(String message = scanner.nextLine(); !message.equals("QUIT"); message = scanner.nextLine()) {
                PrintStream var10000 = System.out;
                String var10001 = connectionToServer.sendForAnswer(message);
                var10000.println("Response from server: " + var10001);
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
