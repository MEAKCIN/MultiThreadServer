package client;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectiontoServer {
    public static final String Default_Server_Adress="localhost";
    public static final int Default_Server_Port=8080;
    private Socket socket;
    protected BufferedReader serverInput;
    protected PrintWriter serverOutput;
    protected String serverAdress;
    protected int serverPort;
    
    public ConnectiontoServer(String serverAdress, int serverPort){
        this.serverAdress = serverAdress;
        this.serverPort = serverPort;
    }



    public void connect() throws Exception {
        socket = new Socket(serverAdress, serverPort);
        serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        serverOutput = new PrintWriter(socket.getOutputStream(), true);
    }
    
    public String sendForAnswers(String message)throws IOException {
        new String();
        this.serverOutput.println(message);
        this.serverOutput.flush();
        String response = this.serverInput.readLine();
        return response;
    }
    public void disconnect() throws IOException{
        this.serverInput.close();
        this.serverOutput.close();
        this.socket.close();
        System.out.println("Connection Closed");
    }
    

}