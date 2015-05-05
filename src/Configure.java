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
        Node root = document.getRootNode();
        List <Node> nodes = root.getChildren();
        List <Node> childs;
        // for root's childs takes the values of attributes
        for (Node curr : nodes) {
            switch (curr.getName()) {
                case "listen":
                    listenPort = (curr.getFirstAttribute()).getValue();
                    break;
                case "statistics":
                    statisticsPort = (curr.getFirstAttribute()).getValue();
                    break;
                case "log":
                    childs = curr.getChildren();
                    for (Node child : childs) {
                        switch (child.getName()) {
                            case "access" :
                                accessPath = (child.getFirstAttribute()).getValue();
                                break;
                            case "error" :
                                errorPath = (child.getFirstAttribute()).getValue();
                                break;
                        }
                    }
                    break;
                case "documentroot":
                    rootPath = (curr.getFirstAttribute()).getValue();
                    break;
            }
        }
    }
    public Integer getlistenPort() {
        return Integer.parseInt(listenPort);
    }
    public Integer getstatisticsPort() {
        return Integer.parseInt(statisticsPort);
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
}
