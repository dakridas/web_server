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
    private String logErrorStr;
    private Log logWrite;
    private StringWriter errors;
    private Server server;

    public ServerThread(Socket socket,String accessPath,String errorPath,String rootPath,Server server) {
        this.server = server;
        this.socket = socket;
        logWrite = new Log(accessPath, errorPath);
        this.accessPath = accessPath;
        this.errorPath = errorPath;
        this.rootPath = rootPath;
    }

    public void run() {
        long start = System.currentTimeMillis();
        Serialize object = new Serialize();
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
            String parametres [] = inputLine.split("\\s");
            pathFile = parametres[1];
            httpVersion = parametres[2];

            if (inputLine.startsWith("GET")) {

                if (!(httpVersion.equals("HTTP/1.0")) && !(httpVersion.equals("HTTP/1.1"))) {
                    server.increaseErrors();
                    logstr = logstr + inputLine + " 400 Bad Request";
                    logWrite.writeToError(logstr);
                    logWrite.closeFiles();
                    send400();
                }
                else {
                    logErrorStr = logstr + inputLine;
                    logstr = logstr + inputLine + " -> 200 OK ";
                    response = new ServerResponse(pathFile,httpVersion,io,rootPath);
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
            // 405 fail
            }else {
                server.increaseErrors();
                logstr = logstr + inputLine + " 405 Method Not Allowed";
                logWrite.writeToError(logstr);
                logWrite.closeFiles();
                send405();
            }
        // 404 fail
        } catch (FileNotFoundException ex) {
            server.increaseErrors();
            errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            logErrorStr = logErrorStr + " 404 Not Found " + errors;
            logWrite.writeToError(logErrorStr);
            logWrite.closeFiles();
            try {
                send404();
            } catch (IOException e) {
                e.printStackTrace();
            }
        // 500 fail
        } catch (IOException ex) {
            server.increaseErrors();
            errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            logErrorStr = logErrorStr + " 500 Internal Server Error" + errors;
            logWrite.writeToError(logErrorStr);
            logWrite.closeFiles();
            send500();
        }

        try {
            socket.close();
            io.closeIO();
        }catch (IOException e) {}

        long end = System.currentTimeMillis();
        server.addResponseTime(end-start);
        server.calcAverageTime();
        object.Serialize(server,"server");
    }

    public void send400() throws IOException{
        String str;
        str = "<!DOCTYPE html> <html> <head> 400 Bad Reqeust </head> <body> <p> Resource Not Found </p> </body> </html>";
        int size = str.length();
        io.writeToSocket(httpVersion + " 200 OK\r\n");
        io.writeToSocket("text/html\r\n");
        io.writeToSocket(Integer.toString(size)+"\r\n");
        io.writeToSocket("\r\n");
        io.writeToSocket(str);
    }

    public void send404() throws IOException{
        String str;
        str = "<!DOCTYPE html> <html> <head> 404 Not Found: </head> <body> <p> Resource Not Found </p> </body> </html>";
        int size = str.length();
        io.writeToSocket(httpVersion + " 200 OK\r\n");
        io.writeToSocket("text/html\r\n");
        io.writeToSocket(Integer.toString(size)+"\r\n");
        io.writeToSocket("\r\n");
        io.writeToSocket(str);
    }

    public void send405() throws IOException{
        String str;
        str = "<!DOCTYPE html> <html> <head> 405 Method Not Allowed </head> </html>";
        int size = str.length();
        io.writeToSocket(httpVersion + " 200 OK\r\n");
        io.writeToSocket("text/html\r\n");
        io.writeToSocket(Integer.toString(size)+"\r\n");
        io.writeToSocket("\r\n");
        io.writeToSocket(str);
    }

    public void send500() {
        String str;
        str = "<!DOCTYPE html> <html> <head> 500 Internal Server Error </head> </html>";
        int size = str.length();
        try {
            io.writeToSocket(httpVersion + " 200 OK\r\n");
            io.writeToSocket("text/html\r\n");
            io.writeToSocket(Integer.toString(size)+"\r\n");
            io.writeToSocket("\r\n");
            io.writeToSocket(str);
        } catch (IOException e) {}
    }
}
