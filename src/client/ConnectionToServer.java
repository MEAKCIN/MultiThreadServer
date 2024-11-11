package client;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ConnectionToServer {
    public static final String DEFAULT_SERVER_ADDRESS = "localhost";
    public static final int DEFAULT_SERVER_PORT = 4444;
    private Socket socket;
    protected BufferedReader inputStream;
    protected PrintWriter outputStream;
    protected String serverAddress;
    protected int serverPort;
    private static final int SOCKET_TIMEOUT= 30000;

    public ConnectionToServer(String address, int port) {
        this.serverAddress = address;
        this.serverPort = port;

    }

    public void connect() throws  IOException {
        this.socket = new Socket(this.serverAddress, this.serverPort);
        this.inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.outputStream = new PrintWriter(this.socket.getOutputStream());
        System.out.println("Successfully connected to server at" + String.valueOf(this.socket.getRemoteSocketAddress()));

    }

    public String sendForAnswer(String message) throws IOException {
        new String();
        this.outputStream.println(message);
        this.outputStream.flush();
        String response = this.inputStream.readLine();
        return response;
    }

    public void disconnect() throws IOException {
        this.inputStream.close();
        this.outputStream.close();
        this.socket.close();
        System.out.println("Connection Closed");
    }
}
