import gr.uth.inf.ce325.xml_parser.Document;
import gr.uth.inf.ce325.xml_parser.DocumentBuilder;
import gr.uth.inf.ce325.xml_parser.Attribute;
import gr.uth.inf.ce325.xml_parser.Namespace;
import gr.uth.inf.ce325.xml_parser.Node;
import java.util.List;

public class Configure {

    private DocumentBuilder db;
    private Document document;
    private String listenPort;
    private String statisticsPort;
    private String accessPath;
    private String errorPath;
    private String rootPath;

    public Configure(String path) {
        db = new DocumentBuilder();
        document = db.getDocument(path);
        System.out.println(document.toXMLString());
        Node root = document.getRootNode();
        System.out.println(root.getName());
        List <Node> nodes = root.getChildren();
        List <Node> childs;
        // for root's childs takes the values of attributes
        for (Node curr : nodes) {
            switch (curr.getName()) {
                case "listen":
                    listenPort = (curr.getFirstAttribute()).getValue();
                    System.out.println(listenPort);
                    break;
                case "statistics":
                    statisticsPort = (curr.getFirstAttribute()).getValue();
                    System.out.println(statisticsPort);
                    break;
                case "log":
                    childs = curr.getChildren();
                    for (Node child : childs) {
                        switch (child.getName()) {
                            case "access" :
                                accessPath = (child.getFirstAttribute()).getValue();
                                System.out.println(accessPath);
                                break;
                            case "error" :
                                errorPath = (child.getFirstAttribute()).getValue();
                                System.out.println(errorPath);
                                break;
                        }
                    }
                    break;
                case "documentroot":
                    rootPath = (curr.getFirstAttribute()).getValue();
                    System.out.println(rootPath);
                    break;
            }
        }
    }
    public String getlistenPort() {
        return listenPort;
    }
    public String getstatisticsPort() {
        return statisticsPort;
    }
    public String getaccessPath() {
        return accessPath;
    }
    public String geterrorPath() {
        return errorPath;
    }
    public String getrootPath() {
        return rootPath;
    }
    public static void main(String[] args) {
        Configure test = new Configure("config.xml");
    }


}
