import java.io.*;
import java.util.*;

public class BuildIndexFile {

    private File file;
    private File [] files;
    private FileOutputStream fileOut;
    private String indexString;

    public BuildIndexFile(String path) {
        openIndexFile(path);
        readDirectory(path);
        writeStyle();
        writeBody(path);
    }
    // create index.html
    private void openIndexFile(String path) {
        try {
            file = new File(path+"index.html");
            file.createNewFile();
            fileOut = new FileOutputStream(file);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    // read directory's files and save them to list
    private void readDirectory(String path) {
        File directory = new File(path);
        files = directory.listFiles();
    }
    // write to index.html style head from style.txt
    private void writeStyle() {
        String inputLine;
        try {
            ServerIO io = new ServerIO();
            io.openFile("style.txt");

            inputLine = io.readFile();
            while ((inputLine = io.readFile()) != null) {
                inputLine = inputLine + "\n";
                fileOut.write(inputLine.getBytes());
                fileOut.flush();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    // write body to index.html
    private void writeBody(String path) {
        try{
            String str;
            str = "    <body>\n";
            fileOut.write(str.getBytes());
            fileOut.flush();
            str = "      <article>\n";
            fileOut.write(str.getBytes());
            fileOut.flush();
            str = "        <ol>\n";
            fileOut.write(str.getBytes());
            fileOut.flush();
            // check path for root directory
            String [] results = path.split("\\/");
            if (results.length > 1) {
                str = String.format("          <li class=\"directory\"><a href=\"%s/\">%s</a></li>\n","..","parent directory");
            }
            fileOut.write(str.getBytes());
            fileOut.flush();
            if (files != null) {
                for (File curr : files) {
                    if (curr.isDirectory()) {
                        str = String.format("          <li class=\"directory\"><a href=\"%s/\">%s</a></li>\n",curr.getName(),curr.getName());
                        fileOut.write(str.getBytes());
                        fileOut.flush();
                    }
                }
                for (File curr : files) {
                    if (!curr.isDirectory() && (!(curr.getName()).equals("index.html"))) {
                        str = String.format("          <li class=\"file\"><a href=\"%s\">%s</a></li>\n",curr.getName(),curr.getName());
                        fileOut.write(str.getBytes());
                        fileOut.flush();
                    }
                }
            }

            fileOut.close();

        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
