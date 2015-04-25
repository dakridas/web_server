import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) throws IOException{

        int portNumber = 8001;
        String HOST_ADDRESS = "172.16.196.1";

        try (ServerSocket serverSocket = new ServerSocket();) {
            serverSocket.bind(new InetSocketAddress("localhost",portNumber));
            try (
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
                ) {
                    System.out.println("Connected to " + clientSocket.getInetAddress().toString()
                        +":"+clientSocket.getPort());
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println(inputLine);
                        System.out.println("echoing: "+inputLine);
                    }
                } catch (IOException e) {
                    System.out.println("Exception caught when trying to listen on port "
                        + portNumber + " or listening for a connection");
                    System.out.println(e.getMessage());
                }
        }
    }
}
