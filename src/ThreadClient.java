import java.net.*;
import java.io.*;

public class ThreadClient implements Runnable {

    private Socket csocket;
    private String pathFile;
    private String httpVersion;

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
            DataOutputStream writeOut = new DataOutputStream(csocket.getOutputStream());
            String inputLine;
            // Get method
            inputLine = in.readLine();
            if (inputLine.startsWith("GET")) {
                // take parametres of GET
                String parametres [] = inputLine.split("\\s");
                pathFile = parametres[1];
                //TODO if has less than 2 parametres
                httpVersion = parametres[2];
                // create responce string here
            // 405 fail
            }else {
                System.out.println("?");
                writeOut.writeBytes("405 Method Not Allowed");
                writeOut.flush();
                writeOut.close();
                csocket.close();
            }

            // wait for header connection and close
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("Connection")) {
                    // send responce string
                    writeOut.flush();
                    writeOut.close();
                    csocket.close();
                }
            }
        } catch (IOException e) {}

    }
}
