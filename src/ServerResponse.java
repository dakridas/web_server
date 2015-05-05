import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ServerResponse {

    private String response;
    private BufferedReader in;
    private Map<String,String> dictionary;

    public ServerResponse(String pathFile,String httpVersion) {
        readMimeFile(pathFile);
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

    public static void main(String[] args) {
        ServerResponse a = new ServerResponse(" "," ");
    }

}
