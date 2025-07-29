import java.io.*;
import java.net.*;

public class ServerCaesar {

    // Decrypt using Caesar cipher with a=0 to z=25 mapping
    static String caesarDecrypt(String text, int key) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            int shifted = (c - 'a' - key + 26) % 26; // add 26 to handle negatives
            char decryptedChar = (char) ('a' + shifted);
            result.append(decryptedChar);
        }

        return result.toString().toUpperCase();
    }

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8080);
        System.out.println("Server started, waiting for connection...");

        Socket socket = server.accept();
        System.out.println("Client connected.");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String received = in.readLine();
        System.out.println("[Received] " + received);

        // Split encrypted message and key
        String[] parts = received.split("::");
        String encrypted = parts[0];
        int key = Integer.parseInt(parts[1]);

        String decrypted = caesarDecrypt(encrypted, key);
        System.out.println("[Decrypted] " + decrypted);

        socket.close();
        server.close();
    }
}