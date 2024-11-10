package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    protected BufferedReader inputStream;
    protected PrintWriter outputStream;
    protected Socket socket;
    private String line=new String();
    private String lines=new String();

    public ServerThread(Socket socket){
        this.socket=socket;
    }


    public void start(){
        try {
            inputStream = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream(), true);
            line=inputStream.readLine();
            while(line.compareTo("QUIT")!=0){
                lines= "Client messaged : " + line + " at : "+ Thread.currentThread().getId();
                outputStream.println(lines);
                outputStream.flush();
                System.out.println("Client "+ socket.getRemoteSocketAddress());

                line=inputStream.readLine();
            }
        } catch(NullPointerException e) {
            line=this.getName();
            System.err.println("Server Thread. Run.Client "+ line +" closed");
        } catch (Exception e) {
            line =this.getName();
            System.out.println("Error starting thread: " + e.getMessage());
        } finally{
            try {
                System.out.println("Closing Connection");
                if(inputStream!= null){
                    inputStream.close();
                    System.err.println("Socket input stream closed");
                }
                if(outputStream!= null){
                    outputStream.close();
                    System.err.println("Socket output stream closed");
                }
                if(socket!= null){
                    socket.close();
                    System.err.println("Socket closed");
                }

            }catch (IOException ie) {
                System.err.println("Socket close error");
            }
        }
    }


}
