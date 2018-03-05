package tcpexample4;

import java.io.*;
import java.net.*;

/**
 *
 * @author Matthew
 */
class TCPClient {

    static TextProtocol textProt;

    public static void main(String argv[]) throws IOException, ClassNotFoundException {
        System.out.println("Client ready!");

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        boolean endLoop = true;
        Socket clientSocket = new Socket("192.168.1.153", 6789);
        ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());

        while (endLoop) {

            System.out.println("\nEnter instruction for server: \n"
                    + "saveMessage to save message to a file \n"
                    + "readFile to read file from server \n"
                    + "closeConnection to terminate connection with server.");
            String action = inFromUser.readLine();
            switch (action) {
                case "saveMessage":
                    System.out.println("Enter message: ");
                    String message = inFromUser.readLine();
                    textProt = new TextProtocol(action, message);
                    outStream.writeObject(textProt);
                    textProt = (TextProtocol) inStream.readObject();
                    System.out.println(textProt.getHeader());
                    break;
                case "readFile":
                    textProt = new TextProtocol(action);
                    outStream.writeObject(textProt);
                    textProt = (TextProtocol) inStream.readObject();
                    for (String line : textProt.getStringArray()) {
                        System.out.println(line);
                    }
                    break;
                case "closeConnection":
                    textProt = new TextProtocol(action);
                    outStream.writeObject(textProt);
                    endLoop = false;
                    break;
                default:
                    System.out.println("Incorrect Input");
                    
            }
        }
        clientSocket.close();
    }
}
