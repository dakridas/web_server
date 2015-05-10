import java.net.*;
import java.io.*;

public class ServerIO {

    private OutputStream socketOut;
    private InputStream socketIn;
    private InputStream fileIn;
    private BufferedReader bufSocketIn;
    private BufferedReader bufFileIn;
    private File file;
    private int sizeFile;
    private String rootPath;
    private boolean deleteFile = false;

    // opens socket stream
    public ServerIO(Socket socket,String rootPath) throws IOException{
        socketIn = socket.getInputStream();
        socketOut = socket.getOutputStream();
        bufSocketIn = new BufferedReader(new InputStreamReader(socketIn));
    }
    public ServerIO() throws IOException{}

    public void writeToSocket(String message) throws IOException{
        socketOut.write(message.getBytes());
        socketOut.flush();
    }

    public void openFile(String path) throws IOException{
        file = new File(path);
        fileIn = new FileInputStream(file);
        bufFileIn = new BufferedReader(new InputStreamReader(fileIn));
        sizeFile = (int)file.length();
    }

    // open index file if exist or create temporary index.html
    public String openIndexFile(String path) {

        String newPath = "";
        boolean fileNotFound = true;

        // directory file
        File directory = new File(path);
        // list of files in directory
        File[] list = directory.listFiles();

        if (list!=null) {
            for (File curr : list) {
                // TODO ignore case
                if ((curr.getName()).equals("index.html")) {
                    newPath = path + "index.html";
                    fileNotFound = false;
                }else if ((curr.getName()).equals("index.htm")) {
                    newPath = path + "index.htm";
                    fileNotFound = false;
                }else if ((curr.getName()).equals("index.php")) {
                    fileNotFound = false;
                }
            }
        }

        if (fileNotFound) {
            BuildIndexFile indexFile = new BuildIndexFile(path);
            newPath = path + "index.html";
            deleteFile = true;
        }

        // open index and save informations
        try {
            file  = new File(newPath);
            fileIn = new FileInputStream(file);
            sizeFile = (int)file.length();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return newPath;
    }

    public void writeFile() throws IOException{
        byte [] bytes;
        int count;

        bytes = new byte[1024];
        count = fileIn.read(bytes,0,1024);
        while (count != -1) {
            socketOut.write(bytes,0,1024);
            count = fileIn.read(bytes,0,1024);
        }
        socketOut.flush();
    }

    public String readSocket() throws IOException{
        return bufSocketIn.readLine();
    }

    public String readFile() throws IOException{
        return bufFileIn.readLine();
    }

    public File getFile() {
        return file;
    }
    public int getSizeFile() {
        return sizeFile;
    }

    public void closeIO() throws IOException{
        System.out.println("Close files");
        socketOut.close();
        socketIn.close();
        bufSocketIn.close();
        // delete temporary file
        if (deleteFile) {
            file.delete();
        }
    }
}
