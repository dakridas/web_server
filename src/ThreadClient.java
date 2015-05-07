import java.net.*;
import java.io.*;

public class ThreadClient implements Runnable {

    private Socket csocket;
    private String pathFile;
    private String httpVersion;
    private String responseString;
    private ServerResponse response;

    public ThreadClient(Socket csocket,String accessPath,String errorPath) {
        this.csocket = csocket;
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(csocket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(csocket.getInputStream()));
            System.out.println("Connected to " + csocket.getInetAddress().toString()
                    +":"+csocket.getPort());
            String inputLine;
            DataOutputStream writeOut = new DataOutputStream(csocket.getOutputStream());
            // Get method
            inputLine = in.readLine();
            if (inputLine.startsWith("GET")) {
                // take parametres of GET
                String parametres [] = inputLine.split("\\s");
                pathFile = parametres[1];
                //TODO if has less than 2 parametres
                httpVersion = parametres[2];
                // create responce string here
                 response = new ServerResponse("test.htm",httpVersion);
            // 405 fail
            }else {
                writeOut.writeBytes("405 Method Not Allowed");
                writeOut.flush();
                writeOut.close();
                csocket.close();
            }

            // wait for header connection and close
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("Connection")) {
                    //writeOut.println(response.getOk());
                    //writeOut.writeBytes(response.getDate());
                    //writeOut.writeBytes(response.getServer());
                    //writeOut.writeBytes(response.getLastModified());
                    //writeOut.writeBytes(response.getConnection());
                    //writeOut.println(response.getContentType());
                    //writeOut.println(response.getContentLength());
                    writeOut.writeBytes(response.getRequestFile());
                    // send responce string
                    writeOut.flush();
                    writeOut.close();
                    csocket.close();
                }
            }
        } catch (IOException e) {}

    }
}
