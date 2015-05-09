import java.net.*;
import java.io.*;

public class Server {

    private Configure settings;
    private int listenPort;
    private int statisticsPort;
    private String accessPath;
    private String errorPath;
    private String rootPath;

    public Server(String path) {
        settings = new Configure(path);
        listenPort = settings.getlistenPort();
        statisticsPort = settings.getstatisticsPort();
        accessPath = settings.getaccessPath();
        errorPath = settings.geterrorPath();
        rootPath = settings.getrootPath();
    }

    public void runServer() {
        try (ServerSocket serverSocket = new ServerSocket();) {
            serverSocket.bind(new InetSocketAddress("localhost",listenPort));
            while(true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ServerThread(clientSocket,accessPath,errorPath)).start();
            }

        } catch (IOException e) {
            System.out.println("Exception caught ");
            System.out.println("when trying to listen on port ");
            System.out.println(listenPort + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}



