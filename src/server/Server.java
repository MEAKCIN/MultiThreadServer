package server;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    public static final int DEFAULT_SERVER_PORT = 4444;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("Oppened up a server socket on " + String.valueOf(Inet4Address.getLocalHost()));
        } catch (IOException var3) {
            IOException e = var3;
            e.printStackTrace();
            System.err.println("Server class.Constructor exception on oppening a server socket");
        }

        while(true) {
            this.listenAndAccept();
        }
    }

    private void listenAndAccept() {
        try {
            Socket socket = this.serverSocket.accept();
            System.out.println("A connection was established with a client on the address of " + String.valueOf(socket.getRemoteSocketAddress()));
            ServerThread st = new ServerThread(socket);
            st.start();
        } catch (Exception var3) {
            Exception e = var3;
            e.printStackTrace();
            System.err.println("Server Class. Connection establishment error inside listen and accept function");
        }

    }
}
