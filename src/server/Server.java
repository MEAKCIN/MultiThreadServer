package server;

import client.ConnectiontoServer;
import client.MultithreadClient;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server
{
    private ServerSocket serverSocket;
    public static final int DEFAULT_SERVER_PORT = 8080;
    private ExecutorService threadPool;


    /**
     * Initiates a server socket on the input port, listens to the line, on receiving an incoming
     * connection creates and starts a ServerThread on the client
     * @param port
     */
    public Server(int port)
    {
        this.threadPool = Executors.newCachedThreadPool();
        try
        {
            serverSocket = new ServerSocket(port);
            System.out.println("Oppened up a server socket on " + Inet4Address.getLocalHost());
            threadPool.submit(()-> new ConnectiontoServer(ConnectiontoServer.Default_Server_Adress,ConnectiontoServer.Default_Server_Port));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("Server class.Constructor exception on oppening a server socket");
        }
        while (true)
        {
            listenAndAccept();
        }
    }

    /**
     * Listens to the line and starts a connection on receiving a request from the client
     * The connection is started and initiated as a ServerThread object
     */
    private void listenAndAccept()
    {
        Socket socket;
        try
        {
            socket = serverSocket.accept();
            System.out.println("A connection was established with a client on the address of " + socket.getRemoteSocketAddress());
            ServerThread serverThread = new ServerThread(socket);
            serverThread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Server Class. Connection establishment error inside listen and accept function");
        }
    }

}

