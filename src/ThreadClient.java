import java.net.*;
import java.io.*;

public class ThreadClient implements Runnable {

    private Socket csocket;

    public ThreadClient(Socket csocket) {
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
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("GET")) {
                   System.out.println("GET");
                }else if ((inputline.startsWith("Connection"))) {
                    System.out.println("Connection");
                }else {
                    continue;
                }
            }
        } catch (IOException e) {}

    }
}
