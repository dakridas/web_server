public class Main {

    public static void main(String[] args) {
        Serialize object = new Serialize();
        Server server = object.Deserialize(new Server(),"server",args[0]);
        server.runServer();
    }

}
