import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Experiment 4 (Server side): TCP client-server communication using
 * ServerSocket and Socket.
 *
 * Compile: javac ChatServer.java ChatClient.java
 * Run    : 1) java ChatServer      (terminal 1)
 *          2) java ChatClient      (terminal 2)
 * Type "bye" on either side to end the chat.
 */
public class ChatServer {

    public static final int PORT = 5000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT + ". Waiting for a client...");

            try (Socket socket = serverSocket.accept();
                 BufferedReader fromClient = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));
                 PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in))) {

                System.out.println("Client connected: " + socket.getInetAddress());

                String message;
                while ((message = fromClient.readLine()) != null) {
                    System.out.println("Client : " + message);
                    if (message.equalsIgnoreCase("bye")) {
                        toClient.println("bye");
                        break;
                    }
                    System.out.print("Server : ");
                    String reply = keyboard.readLine();
                    toClient.println(reply);
                    if (reply == null || reply.equalsIgnoreCase("bye")) break;
                }
                System.out.println("Chat ended.");
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
