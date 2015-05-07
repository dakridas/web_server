import java.io.*;
import java.util.*;
import java.text.*;

public class ServerResponse {

    private String response;
    private String requestFile;
    private BufferedReader in;
    private Map<String,String> dictionary;
    private String ok = " 200 OK";
    private String date = "Date: ";
    private String server = "Server: CE325 (Java based server)";
    private String lastModified = "Last-Modified: ";
    private String contentLength = "Content-Length: ";
    private String connection = "Connection: close";
    private String contentType = "Content-Type: ";
    private SimpleDateFormat ft;

    public ServerResponse(String pathFile,String httpVersion) {
        ok = httpVersion + ok;
        readCurrentDate();
        readMimeFile();
        getContentType(pathFile);
        readRequestFile(pathFile);
        createRespose();
    }

    public String getOk() {
        return ok;
    }
    public String getDate() {
        return date;
    }
    public String getServer() {
        return server;
    }
    public String getLastModified() {
        return lastModified;
    }
    public String getContentLength() {
        return contentLength;
    }
    public String getConnection() {
        return connection;
    }
    public String getContentType() {
        return contentType;
    }
    public String getRequestFile() {
        return requestFile;
    }

    private void createRespose() {
        response = ok + date + server + lastModified + contentLength + connection + contentType;
        response = response + " " + requestFile;
    }
    // get content type
    private void getContentType(String pathFile) {
        String extension = "";
        int i = pathFile.lastIndexOf('.');
        if (i > 0) {
                extension = pathFile.substring(i+1);
        }
        contentType = contentType + dictionary.get(extension);
    }
    // read current date
    private void readCurrentDate() {
        ft = new SimpleDateFormat("E',' dd MMMM yyyy hh:mm:ss 'GMT' ");
        ft.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dnow = new Date();
        date = date + ft.format(dnow);
    }
    // read mime types to a dictionary
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
    // Read request file and last modified date
    private void readRequestFile(String pathFile) {
        StringBuilder everything = new StringBuilder();
        String line;
        File file = new File(pathFile);
        //contentLength = contentLength + String.valueOf(file.length());
        lastModified = lastModified + ft.format(file.lastModified());
        try {
            in = new BufferedReader(new FileReader(file));
            while ((line = in.readLine()) != null) {
                everything.append(line);
            }
            requestFile = everything.toString();
            contentLength = contentLength + requestFile.length();
        } catch (IOException e) {
            System.out.println("File not found at " +  pathFile);
        }
    }


    public static void main(String[] args) {
        ServerResponse a = new ServerResponse("test.htm","HTTP/1.1");
    }

}
