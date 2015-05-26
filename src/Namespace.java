// Dimitris Akridas d.akridas@gmail.com
// Aristotelis Koligliatis aris7kol@yahoo.gr
import java.lang.Object;

public class Namespace extends Object {

    private String prefix;
    private String uri;

    public Namespace(String prefix, String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getURI() {
        return uri;
    }

    public String toXMLString() {
        return ("Namespace: xmlns:"+prefix+"="+uri);
    }


}
