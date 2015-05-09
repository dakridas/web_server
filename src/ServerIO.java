import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class ServerIO {

    private OutputStream socketOut;
    private InputStream socketIn;
    private InputStream fileIn;
    private BufferedReader bufSocketIn;
    private File file;
    private int sizeFile;
    private String rootPath;
    private boolean deleteFile = false;

    public ServerIO(Socket socket,String rootPath) throws IOException{
        socketIn = socket.getInputStream();
        socketOut = socket.getOutputStream();
        bufSocketIn = new BufferedReader(new InputStreamReader(socketIn));
    }

    public void writeString(String message) throws IOException{
        socketOut.write(message.getBytes());
        socketOut.flush();
    }

    public void openFile(String path) throws IOException{
        file = new File(path);
        fileIn = new FileInputStream(file);
        sizeFile = (int)file.length();
    }

    // open index file if exist or open default index.html
    public String openIndexFile(String path) {

        String newPath = "";
        try {
            newPath = path + "index.html";
            file  = new File(newPath);
            fileIn = new FileInputStream(file);
            sizeFile = (int)file.length();
        } catch (FileNotFoundException e) {
            try {
                newPath = path + "index.htm";
                file  = new File(newPath);
                fileIn = new FileInputStream(file);
                sizeFile = (int)file.length();
            } catch (FileNotFoundException a ) {
                try{
                    newPath = rootPath + "index.html";
                    BuildIndexFile index = new BuildIndexFile(rootPath);
                    file = index.getFile();
                    fileIn = new FileInputStream(file);
                    sizeFile = (int)file.length();
                    deleteFile = true;
                }catch (FileNotFoundException b) {
                }
            }

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

    public String readLine() throws IOException{
        return bufSocketIn.readLine();
    }

    public File getFile() {
        return file;
    }
    public int getSizeFile() {
        return sizeFile;
    }

    public void closeIO() throws IOException{
        socketOut.close();
        socketIn.close();
        bufSocketIn.close();
        if (deleteFile) {
            file.delete();
        }
    }
}
