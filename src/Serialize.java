import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Serialize {

    // Serialize object
    public void Serialize(Server server, String filename) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filename + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(server);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            System.out.println("Serialize Error");
            i.printStackTrace();
        }
        //System.out.println(filename + server);
    }

    // Deserialize object
    public Server Deserialize(Server server, String filename, String arg) {
        try{
            File file = new File(filename + ".ser");
            if (!file.exists()) {
                file.createNewFile();
                return (new Server(arg));
            }else {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                server = (Server) in.readObject();
                in.close();
                fileIn.close();
            }
        } catch (IOException i) {
            System.out.println("Deserialize Error");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Serialize.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(filename + server);
        return server;
    }

}
