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
    private long averageResponseTime;
    private long totalResponseTime;

    public Server(String path) {
        settings = new Configure(path);
        listenPort = settings.getlistenPort();
        statisticsPort = settings.getstatisticsPort();
        accessPath = settings.getaccessPath();
        errorPath = settings.geterrorPath();
        rootPath = settings.getrootPath();
    }

    public Server(){}

    public void increaseErrors(){
        errors++;
    }

    public void addResponseTime(long time) {
        totalResponseTime = totalResponseTime + time;
    }

    public void calcAverageTime() {
        averageResponseTime = totalResponseTime / connections;
    }

    public void runServer() {
        System.out.println("Total running time in seconds : "+onTime);
        System.out.println("Total connections : "+connections);
        System.out.println("Total errors : "+errors);
        System.out.println("Average response time in milliseconds : "+averageResponseTime);

        long startTime;
        try (ServerSocket serverSocket = new ServerSocket();) {
            serverSocket.bind(new InetSocketAddress("localhost",listenPort));
            while(true) {
                startTime = (System.currentTimeMillis())/1000;
                Socket clientSocket = serverSocket.accept();
                new Thread(new ServerThread(clientSocket,accessPath,errorPath,rootPath,this)).start();
                connections++;
                onTime = onTime + ((System.currentTimeMillis()/1000) - startTime);
            }

        } catch (IOException e) {
            System.out.println("Exception caught ");
            System.out.println("when trying to listen on port ");
            System.out.println(listenPort + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}



