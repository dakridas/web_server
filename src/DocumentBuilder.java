// Dimitris Akridas d.akridas@gmail.com
// Aristotelis Koligliatis aris7kol@yahoo.gr
package gr.uth.inf.ce325.xml_parser;
import java.lang.Object;
import java.io.*;
import java.lang.*;
import java.net.*;
import java.util.regex.*;

public class DocumentBuilder extends Object implements Serializable{

    private static final long serialVersionUID = 42L;

    public Document getDocument(String location) {
        String documentStr = getDocumentAsString(location);
        Document document = parseDocument(documentStr);
        return document;
    }

    public String getDocumentAsString(String location) {
        BufferedReader in;
        try {
            //TODO change condition (if string is url)
            if (location.contains("http:")) {
                URL oracle = new URL(location);
                in = new BufferedReader(new InputStreamReader(oracle.openStream()));
            }else {
                File file = new File(location);
                FileReader fReader = new FileReader(file);
                in = new BufferedReader(fReader);
            }

            String inputLine;
            StringBuffer strDocument = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                strDocument.append(inputLine);
            }
            return strDocument.toString();
        }catch (FileNotFoundException ex) {
            System.out.println("The specified file was not found at "+ location);
            return "";
        }catch (IOException ex) {
            System.out.println("IOException occured while reading from file "+ location);
        }
        return "Nothing to return...";

    }

   public Document parseDocument(String documentStr) {

       // Document object
       Document document = new Document();
       // Pattern of node and atribute
       Pattern nodeP = Pattern.compile("<(/?)([a-zA-Z_]+)([a-zA-Z_0-9:]*)([^>]*)(/?)>");
       Matcher nodeM = nodeP.matcher(documentStr);
       // Pattern of namespace
       Pattern namespaceP = Pattern.compile("xmlns:([a-zA-z_]+)([a-zA-Z_0-9]*)(=\")([\\p{Alnum}\\p{Punct}]+)(\")([\\s]*)");
       Matcher namespaceM = namespaceP.matcher(documentStr);
       // Pattern text
       Pattern textP = Pattern.compile("(.*)");
       Matcher textM;
       // temp variables
       Node current = null;
       Node temp = null;
       Namespace namespace = null;
       String str = null;
       // region's variables
       int start = 0;
       int end = 0;

       // Parse namespace
       while (namespaceM.find()) {
           namespace = new Namespace(namespaceM.group(1),namespaceM.group(4));
           document.addNamespace(namespace);
       }
       // Parse node, atributes and text
       while (nodeM.find()) {
           // root node
           if (current == null) {
               current = new Node(document,nodeM.group());
               document.setRootNode(current);
               current.setParent(new Node());
               // set start of region
               start = nodeM.end();
           }else {
               // parse node "<../>"
               if ((nodeM.group()).contains("/>")) {
                   // set end of region
                   end = nodeM.start();
                   // parse text
                   textM = textP.matcher(documentStr).region(start,end);
                   textM.find();
                   // set text
                   current.setText(current.getText() + textM.group());
                   // new node
                   str = nodeM.group().replaceAll("/>","") + ">";
                   temp = new Node(document,str);
                   // set parent
                   temp.setParent(current);
                   // add child
                   current.addChild(temp);
                   // set start of region
                   start = nodeM.end();
               // end of node
               }else if (nodeM.group(1).equals("/"))  {
                   // set end of region
                   end = nodeM.start();
                   // parse text
                   textM = textP.matcher(documentStr).region(start,end);
                   textM.find();
                   current.setText(current.getText() + textM.group());
                   // return to parent
                   current = current.getParent();
                   // set end of region
                   start = nodeM.end();
               // new node
               }else {
                   temp = new Node(document,nodeM.group());
                   temp.setParent(current);
                   current.addChild(temp);
                   // set end of region
                   end = nodeM.start();
                   // parse text
                   textM = textP.matcher(documentStr).region(start,end);
                   textM.find();
                   current.setText(current.getText() + textM.group());
                   current = temp;
                   // set start of region
                   start = nodeM.end();
               }
           }
       }
       return document;
   }

}
