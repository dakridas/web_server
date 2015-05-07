import java.io.*;
import java.util.*;
import java.text.*;

public class ServerResponse {

    private String response;
    private BufferedReader in;
    private Map<String,String> dictionary;

    public ServerResponse(String pathFile,String httpVersion) {
        readMimeFile(pathFile);
        readCurrentDate();
    }

    public String getRespose() {
        return response;
    }

    private void readMimeFile(String pathFile) {
        dictionary = new HashMap<String,String>();
        try {
            in = new BufferedReader(new FileReader("MIME.txt"));
            String line;
            while ((line = in.readLine()) != null) {
                String tokens [] = line.split("\\s+");
                dictionary.put(tokens[0],tokens[1]);
            }
        } catch (IOException e) {
            System.out.println("File not found at " +  pathFile);
        }
    }

    private void readCurrentDate() {
        Date dnow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E',' dd MMMM yyyy hh:mm:ss 'GMT' ");
        ft.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = ft.format(dnow);
    }
    public static void main(String[] args) {
        ServerResponse a = new ServerResponse(" "," ");
    }

}
