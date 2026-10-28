import java.io.*;
import java.net.Socket;

/**
 * Experiment 4 (Client side): connects to ChatServer using a Socket.
 * Start ChatServer first, then run: java ChatClient [host]
 */
public class ChatClient {

    public static void main(String[] args) {
        String host = (args.length > 0) ? args[0] : "localhost";

        try (Socket socket = new Socket(host, ChatServer.PORT);
             BufferedReader fromServer = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server " + host + ":" + ChatServer.PORT);

            String message;
            while (true) {
                System.out.print("Client : ");
                message = keyboard.readLine();
                if (message == null) break;
                toServer.println(message);
                if (message.equalsIgnoreCase("bye")) break;

                String reply = fromServer.readLine();
                if (reply == null) break;
                System.out.println("Server : " + reply);
                if (reply.equalsIgnoreCase("bye")) break;
            }
            System.out.println("Chat ended.");
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage()
                    + "  (is the server running?)");
        }
    }
}
