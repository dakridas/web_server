import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class ServerThread implements Runnable {

    private Socket socket;
    private String pathFile;
    private String rootPath;
    private String errorPath;
    private String accessPath;
    private String httpVersion;
    private String responseString;
    private ServerResponse response;
    private String inputLine;
    private ServerIO io;
    private SimpleDateFormat ft;
    private Date dnow;
    private String logstr;
    private Log logWrite;

    public ServerThread(Socket socket,String accessPath,String errorPath,String rootPath) {
        this.socket = socket;
        logWrite = new Log(accessPath, errorPath);
        this.accessPath = accessPath;
        this.errorPath = errorPath;
        this.rootPath = rootPath;
    }

    public void run() {
        try {
            io = new ServerIO(socket,rootPath);
            System.out.printf("Connected to ");
            System.out.printf(socket.getInetAddress().toString());
            System.out.printf(":"+socket.getPort()+"\n");

            logstr = socket.getInetAddress().toString() + " - [";
            ft = new SimpleDateFormat("E MMMM dd hh:mm:ss zzz yyyy");
            Date dnow = new Date();
            logstr = logstr + ft.format(dnow) + "] ";

            // read the input
            inputLine = io.readSocket();
            if (inputLine.startsWith("GET")) {
                String parametres [] = inputLine.split("\\s");
                pathFile = parametres[1];
                httpVersion = parametres[2];

                if (!(httpVersion.equals("HTTP/1.0")) && !(httpVersion.equals("HTTP/1.1"))) {
                    logstr = logstr + inputLine + " 400 Bad Request";
                    logWrite.writeToError(logstr);
                    logWrite.closeFiles();
                }
                else {
                    logstr = logstr + inputLine + " -> 200 OK ";
                    response = new ServerResponse(pathFile,httpVersion,io);
                    // wait for header connection and close
                    while ((inputLine = io.readSocket()) != null) {
                        if (inputLine.startsWith("User-Agent")) {
                            logstr = logstr + "\"" + inputLine + "\"";
                        }
                        if (inputLine.startsWith("Connection")) {
                            response.sendResponse();
                            break;
                        }
                    }
                    logWrite.writeToAccess(logstr);
                    logWrite.closeFiles();
                }
                socket.close();
            // 405 fail
            }else {
                logstr = logstr + inputLine + " 405 Method Not Allowed";
                logWrite.writeToError(logstr);
                logWrite.closeFiles();
                socket.close();
            }
            // close io
            io.closeIO();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                io.closeIO();
            } catch (IOException d) {
            }
        }


    }
}
