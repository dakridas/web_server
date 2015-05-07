import java.io.*;
import java.util.*;
import java.text.*;

public class ServerResponse {

    private String response;
    private String requestFile;
    private BufferedReader in;
    private Map<String,String> dictionary;
    private String Date = "Date: ";
    private String LastModified = "Last-Modified: ";
    private String ContentLength = "Content-Length: ";
    private String Connection = "Connection: ";
    private String ContentType = "Content-Type: ";

    public ServerResponse(String pathFile,String httpVersion) {
        readMimeFile();
        readRequestFile(pathFile);
        createRespose();
    }

    public String getResponse() {
        return response;
    }

    private void createRespose() {


    }

    private void readMimeFile() {
        dictionary = new HashMap<String,String>();
        try {
            in = new BufferedReader(new FileReader("MIME.txt"));
            String line;
            while ((line = in.readLine()) != null) {
                String tokens [] = line.split("\\s+");
                dictionary.put(tokens[0],tokens[1]);
            }
        } catch (IOException e) {
            System.out.println("File not found at " +  "MIME.txt");
        }
    }

    private void readRequestFile(String pathFile) {
        StringBuilder everything = new StringBuilder();
        String line;
        File file = new File(pathFile);
        LastModified = LastModified + file.lastModified();
        System.out.println(LastModified);

        try {
            in = new BufferedReader(new FileReader(file));
            while ((line = in.readLine()) != null) {
                everything.append(line);
            }
            requestFile = everything.toString();
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
        ServerResponse a = new ServerResponse("test.htm"," ");
    }

}
