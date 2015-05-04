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
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                System.out.println("echoing: "+inputLine);
                System.out.println("?");
            }
            System.out.println("#");
        } catch (IOException e) {}

    }
}
