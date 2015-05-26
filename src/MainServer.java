// Dimitris Akridas d.akridas@gmail.com
// Aristotelis Koligliatis aris7kol@yahoo.gr
public class MainServer{
    public MainServer(String configPath){
        Serialize object = new Serialize();
        Server server = object.Deserialize(new Server(),"server",configPath);
        server.runServer();
    }
}
