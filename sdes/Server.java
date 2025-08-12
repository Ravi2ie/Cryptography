package sdes;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started. Waiting for connection...");
            Socket socket = serverSocket.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String key10 = in.readLine();
            String plaintext = in.readLine();
            System.out.println("Received key: " + key10);
            System.out.println("Received plaintext: " + plaintext);

            String[] keys = SDES.generateSubkeys(key10);
            String k1 = keys[0];
            String k2 = keys[1];

            String encrypted = SDES.encrypt(plaintext, k1, k2);
            System.out.println("Encrypted text: " + encrypted);

            // Send back encrypted + keys
            out.println(encrypted);
            out.println(k1);
            out.println(k2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
