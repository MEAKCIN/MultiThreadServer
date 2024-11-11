package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    protected BufferedReader inputStream;
    protected PrintWriter outputStream;
    protected Socket socket;
    private String line = new String();
    private String lines = new String();

    public ServerThread(Socket s) {
        this.socket = s;
    }

    public void run() {
        try {
            this.inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.outputStream = new PrintWriter(this.socket.getOutputStream());

            for(this.line = this.inputStream.readLine(); this.line.compareTo("QUIT") != 0; this.line = this.inputStream.readLine()) {
                String var10001 = this.line;
                this.lines = "Client messaged : " + var10001 + " at  : " + Thread.currentThread().getId();
                this.outputStream.println(this.lines);
                this.outputStream.flush();
                PrintStream var10000 = System.out;
                var10001 = String.valueOf(this.socket.getRemoteSocketAddress());
                var10000.println("Client " + var10001 + " sent :  " + this.lines);
            }
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
}
