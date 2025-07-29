import java.io.*;
import java.net.*;

public class ServerCaesarAttack {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8030);
        System.out.println("Server running... Waiting for Caesar brute-force attack results.");

        Socket socket = serverSocket.accept();
        System.out.println("Client connected.");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }

        socket.close();
        serverSocket.close();
        System.out.println("Server closed.");
    }
}