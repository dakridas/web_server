import java.net.*;
import java.io.*;

public class Server implements Serializable {
    private Configure settings;
    private int listenPort;
    private int statisticsPort;
    private String accessPath;
    private String errorPath;
    private String rootPath;
    // statistics
    private long onTime;
    private int connections;
    private int errors;
    private long averageResponceTime;

    public Server(String path) {
        settings = new Configure(path);
        listenPort = settings.getlistenPort();
        statisticsPort = settings.getstatisticsPort();
        accessPath = settings.getaccessPath();
        errorPath = settings.geterrorPath();
        rootPath = settings.getrootPath();
    }

    public Server(){}

    public void incriseErrors(){
        errors++;
    }

    public void runServer() {
        System.out.println("Total running time :"+onTime+" seconds");
        System.out.println("Total connections :"+connections);

        long startTime;
        Serialize object = new Serialize();
        try (ServerSocket serverSocket = new ServerSocket();) {
            serverSocket.bind(new InetSocketAddress("localhost",listenPort));
            while(true) {
                startTime = (System.currentTimeMillis())/1000;
                Socket clientSocket = serverSocket.accept();
                new Thread(new ServerThread(clientSocket,accessPath,errorPath,rootPath)).start();
                connections++;
                onTime = onTime + ((System.currentTimeMillis()/1000) - startTime);
                object.Serialize(this,"server");
            }

        } catch (IOException e) {
            System.out.println("Exception caught ");
            System.out.println("when trying to listen on port ");
            System.out.println(listenPort + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}



