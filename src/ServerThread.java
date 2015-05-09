import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class ServerThread implements Runnable {

    private Socket socket;
    private String pathFile;
    private String httpVersion;
    private String responseString;
    private ServerResponse response;
    private String inputLine;
    private ServerIO io;

    public ServerThread(Socket socket,String accessPath,String errorPath) {
        this.socket = socket;
    }

    public void run() {
        try {
            io = new ServerIO(socket);
            System.out.printf("Connected to ");
            System.out.printf(socket.getInetAddress().toString());
            System.out.printf(":"+socket.getPort()+"\n");
            // read the input
            inputLine = io.readLine();
            if (inputLine.startsWith("GET")) {
                String parametres [] = inputLine.split("\\s");
                pathFile = parametres[1];
                httpVersion = parametres[2];
                response = new ServerResponse(pathFile,httpVersion,io);
                // wait for header connection and close
                while ((inputLine = io.readLine()) != null) {
                    if (inputLine.startsWith("Connection")) {
                        response.sendResponse();
                        socket.close();
                    }
                }
            // 405 fail
            }else {
                io.writeString("405 Method Not Allowed");
            }
            // close io
            io.closeIO();
        } catch (IOException e) {}


    }
}
