import java.io.*;

public class Log {

    private PrintWriter outputStreamAccess;
    private PrintWriter outputStreamError;

    public Log (String accessPath, String errorPath) {

        try {
            outputStreamAccess = new PrintWriter(new BufferedWriter(new FileWriter(accessPath, true)));
        }catch (IOException e) {
            try {
                outputStreamAccess = new PrintWriter(new FileWriter(new File(accessPath)));
            } catch (IOException ex) {
                System.out.println("Error");
            }
        }

        try {
            outputStreamError = new PrintWriter(new BufferedWriter(new FileWriter(errorPath, true)));

        }catch (IOException e) {
            try {
            outputStreamError = new PrintWriter(new FileWriter(new File(errorPath)));
            }catch (IOException ex) {
                System.out.println("Error");
            }
        }

    }

    public void writeToAccess(String inputString) {
        try {
            outputStreamAccess.println(inputString);
        } catch (Exception e) {
            System.out.println("Error writing in access file");
        }

    }

    public void writeToError(String inputString) {
        try {
            outputStreamError.println(inputString);
        } catch (Exception e) {
            System.out.println("Error writing in error file");
        }
    }

    public void closeFiles()
    {
        outputStreamAccess.flush();
        outputStreamError.flush();
        outputStreamAccess.close();
        outputStreamError.close();
    }

    public static void main(String[] args) {

        Log write = new Log("access/log.txt", "error/log.txt");
        write.writeToAccess("aris");
        write.writeToAccess("mitsos");
        write.writeToError("aris!");
        write.writeToError("mitsos!");
        write.closeFiles();

    }

}
