// Dimitris Akridas d.akridas@gmail.com
// Aristotelis Koligliatis aris7kol@yahoo.gr
import java.lang.Object;
import java.util.List;
import java.util.LinkedList;
import java.util.regex.*;
import java.io.*;
import java.util.StringTokenizer;

public class Node extends Object implements Serializable{

    private static final long serialVersionUID = 42L;

    private Document doc;
    private String nodeStr;
    private Node parent;
    private Namespace n;
    private String text;
    private String name;

    private List <Attribute> attributes;
    private List <Node> childs;

    private int index_attributes;
    private int index_childs;
    // Constructors
    public Node() {
        attributes = new LinkedList <Attribute>();
        childs = new LinkedList <Node>();
        text = "";
    }
    public Node(Document doc, String nodeStr) {
        this.doc = doc;
        this.nodeStr = nodeStr;
        attributes = new LinkedList <Attribute>();
        childs = new LinkedList <Node>();
        text = "";
        parseNode(nodeStr);
    }
    public Node(Document doc, String nodeStr, Node parent) {
        this.doc = doc;
        this.nodeStr = nodeStr;
        this.parent = parent;
        attributes = new LinkedList <Attribute>();
        childs = new LinkedList <Node>();
        text = "";
        parseNode(nodeStr);
    }
    public Node(Document doc, String name, String text) {
        this.doc = doc;
        this.name = name;
        this.text = text;
    }
    public Node(Document doc, String name, String text, List<Attribute> attrs) {
        this.doc = doc;
        this.name = name;
        this.text = text;
        this.attributes = attrs;
    }
    public Node(Document doc, String name, String text, List<Attribute> attrs,Namespace nm) {
        this.doc = doc;
        this.name = name;
        this.text = text;
        this.attributes = attrs;
        this.n = nm;
    }
    public Node(String name) {
        this.name = name;
    }
    public Node(String name, String text) {
        this.name = name;
        this.text = text;
    }
    public Node(String name, String text, List<Attribute> attrs, Namespace nm) {
        this.name = name;
        this.text = text;
        this.attributes = attrs;
        this.n = nm;
    }
    public Node(String name, String text, Node parent) {
        this.name = name;
        this.text = text;
        this.parent = parent;
    }
    public Node(String name, String text, Node parent, List<Attribute> attrs) {
        this.name = name;
        this.text = text;
        this.parent = parent;
        this.attributes = attrs;
    }
    public Node(String name, String text, Node parent, List<Attribute> attrs, Namespace nm) {
        this.name = name;
        this.text = text;
        this.parent = parent;
        this.attributes = attrs;
        this.n = nm;
    }
    // Print Methods
    public void printChildrens() {
        if (childs.size() != 0 ) {
            System.out.println(name+":");
            for (int i = 0; i < childs.size(); i++) {
                System.out.println("        "+ (childs.get(i)).getName());
            }
        }
    }
    public void printAttributes() {
        if (attributes.size() != 0 ) {
            System.out.println(name+":");
            for (int i = 0; i < attributes.size(); i++) {
                System.out.println("        "+ (attributes.get(i)).getName());
                System.out.println("        "+ (attributes.get(i)).getValue());
            }
        }
    }
    // API Methods
    public void addAttribute(Attribute attr) {
        attributes.add(attr);
    }

    public void addAttribute(int index, Attribute attr) {
        attributes.add(index,attr);
    }

    public void addChild(int index, Node child) {
        childs.add(index,child);
    }

    public void addChild(Node child) {
        childs.add(child);
    }

    public Attribute getAttribute(int index) {
        return attributes.get(index);
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public Node getChild(int index) {
        return childs.get(index);
    }

    public List<Node> getChildren() {
        return childs;
    }

    public Attribute getFirstAttribute() {
        return attributes.get(0);
    }

    public Node getFirstChild() {
        return childs.get(0);
    }

    public String getName() {
        return this.name;
    }

    public String getNodeStr() {
        return this.nodeStr;
    }

    public Namespace getNamespace() {
        return n;
    }

    public Attribute getNextAttributes() {
        Attribute temp;
        if (index_attributes == attributes.size()) {
            index_attributes = 0;
            return null;
        }
        temp = attributes.get(index_attributes);
        index_attributes++;
        return temp;
    }

    public Node getNextChild() {
        Node temp;
        if (index_childs == childs.size()) {
            index_childs = 0;
            return null;
        }
        temp = childs.get(index_childs);
        index_childs++;
        return temp;
    }

    public Node getParent() {
        return parent;
    }

    public String getText() {
        return text;
    }

    protected void parseNode(String nodeStr) {
        Pattern nodeP = Pattern.compile("<(/?)([a-zA-Z_]+)([a-zA-Z_0-9:]*)([^>]*)(/?)>");
        Matcher nodeM = nodeP.matcher(nodeStr);
        nodeM.find();
        // if node has prefix save namespace that prefix belongs to
        if (!(nodeM.group(3).equals(""))) {
            n = doc.getNamespace(nodeM.group(2));
        }
        // the node's name if has prefix contains the prefix
        boolean isWhitespace = nodeM.group(3).matches("^\\s*$");
        if (!isWhitespace) {
            name = nodeM.group(3);
            name = name.replace(":","");
        }else {
            name = nodeM.group(2);
        }
        // save all the attributes that the node has
        if (!nodeM.group(4).equals("")) {
            StringTokenizer st = new StringTokenizer(nodeM.group(4));
            while (st.hasMoreTokens()) {
                addAttribute(new Attribute(st.nextToken()));
            }
        }
    }

    public void setName(String name) {
        this.name  = name;
    }

    public void setNamespace(Namespace n) {
        this.n = n;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setText(String text) {
        boolean isWhitespace = text.matches("^\\s*$");
        if (!isWhitespace) {
            this.text = text;
        }
    }

    public String toXMLString() {
        String str = new String();
        for (int i = 0; i < attributes.size(); i++) {
            str = str + " " + (attributes.get(i)).toXMLString();
        }
        return (str);
    }

    public String toXMLString(int depth) {
        String str = new String();
        String spaces = new String();
        str = "<" + name + toXMLString() + ">";
        for (int i = 0; i < depth; i++) {
            spaces = spaces + "  ";
        }
        for (int i = 0; i < childs.size(); i++) {
            str = str + "\n" + (childs.get(i)).toXMLString(depth + 1);
        }
        if (childs.size() > 0 ) {
            if (text.equals("")) {
                str = str + "\n" + spaces + "</" + name + ">";
            }else {
                str = str + "\n" + spaces + text + "\n" + spaces + "</" + name + ">";
            }
        }else {
            str = str + text + "</" + name + ">";
        }

        return (spaces + str);
    }

}
