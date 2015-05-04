import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) {

        int portNumber = 8000;

        try (ServerSocket serverSocket = new ServerSocket();) {
            serverSocket.bind(new InetSocketAddress("localhost",portNumber));
            while(true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ThreadClient(clientSocket)).start();
                System.out.println("###############");
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}



