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

    public ServerIO(Socket socket) throws IOException{
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
    }
}
