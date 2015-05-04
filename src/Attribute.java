// Dimitris Akridas d.akridas@gmail.com
// Aristotelis Koligliatis aris7kol@yahoo.gr
package gr.uth.inf.ce325.xml_parser;
import java.lang.Object;
import java.util.regex.*;

public class Attribute extends Object {

	private String name;
	private String value;
	private Namespace nm;
	private Document doc;
	private String attributeStr;

    // Constructors
    public Attribute(Document doc, String attributeStr) {
		this.doc = doc;
		this.attributeStr = attributeStr;
        // Parse name and value of attribute
        Pattern attrP = Pattern.compile(("([a-zA-z_]+)=\"([\\p{Alnum}\\p{Punct}]+)\""));
        Matcher attrM = attrP.matcher(attributeStr);
        while(attrM.find()) {
            name = attrM.group(1);
            value = attrM.group(2);
        }
	}

	public Attribute(String attributeStr) {
		this.attributeStr = attributeStr;
        // Parse name and value of attribute
        Pattern attrP = Pattern.compile(("([a-zA-z_]+)=\"([\\p{Alnum}\\p{Punct}]+)\""));
        Matcher attrM = attrP.matcher(attributeStr);
        while(attrM.find()) {
            name = attrM.group(1);
            value = attrM.group(2);
        }
	}

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Attribute(String name, String value, Document doc) {
        this.name = name;
        this.value = value;
        this.doc = doc;
    }

    public Attribute(String name, String value, Document doc, Namespace nm) {
        this.name = name;
        this.value = value;
        this.doc = doc;
        this.nm = nm;
    }

    public Attribute(String name, String value, Namespace nm) {
        this.name = name;
        this.value = value;
        this.nm = nm;
    }


    // Methods
	public String getName() {
		 return name;
	}

	public Namespace getNamespace() {
		return nm;
	}

	public String getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNamespace(Namespace nm) {
		this.nm = nm;
	}

	public void setValue() {
		this.value = value;
	}

	public String toXMLString() {
        return name+"="+value;
	}
}
