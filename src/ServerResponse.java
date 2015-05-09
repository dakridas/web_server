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
    private String server = "Server: CE325 (Java based server)\r\n";
    private String lastModified = "Last-Modified: ";
    private String contentLength = "Content-Length: ";
    private String connection = "Connection: close\r\n";
    private String contentType = "Content-Type: ";
    private SimpleDateFormat ft;
    private ServerIO io;
    private boolean isDirectory;

    public ServerResponse(String pathFile,String httpVersion,ServerIO io) throws IOException {
        this.io = io;
        pathFile = pathFile.replaceFirst("[/]","");
        if (pathFile.endsWith("/")) {
            isDirectory = true;
        }
        System.out.println(pathFile);
        ok = httpVersion + ok + "\r\n";
        readCurrentDate();
        readMimeFile();
        if (isDirectory) {
            pathFile = io.openIndexFile(pathFile);
            getContentType(pathFile);
        }else {
            io.openFile(pathFile);
            getContentType(pathFile);
        }
        readFileInformations();
    }

    public void sendResponse() throws IOException{
        io.writeString(ok);
        io.writeString(date);
        io.writeString(server);
        io.writeString(connection);
        io.writeString(lastModified);
        io.writeString(contentType);
        io.writeString(contentLength);
        io.writeString("\r\n");
        io.writeFile();
    }
    // get content type
    private void getContentType(String pathFile) {
        String extension = "";
        int i = pathFile.lastIndexOf('.');
        if (i > 0) {
                extension = pathFile.substring(i+1);
        }
        contentType = contentType + dictionary.get(extension) + "\r\n";
    }
    // read current date
    private void readCurrentDate() {
        ft = new SimpleDateFormat("E',' dd MMMM yyyy hh:mm:ss 'GMT' ");
        ft.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dnow = new Date();
        date = date + ft.format(dnow) + "\r\n";
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
    // Read file informations
    private void readFileInformations() {
        lastModified = lastModified + ft.format((io.getFile()).lastModified()) + "\r\n";
        contentLength = contentLength + io.getSizeFile() + "\r\n";
    }
}
