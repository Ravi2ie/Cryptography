import java.io.*;
import java.net.*;

public class ServerAffine {

    // Find modular inverse of a under mod 26
    static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1)
                return x;
        }
        return -1; // no inverse exists
    }

    static String affineDecrypt(String text, int a, int b) {
        StringBuilder result = new StringBuilder();
        int a_inv = modInverse(a, 26); // modular inverse of a

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                int y = c - 'A'; // Convert uppercase letter to 0–25
                int x = (a_inv * (y - b + 26)) % 26;
                result.append((char)(x + 'a')); // convert back to lowercase
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8080);
        System.out.println("Server started, waiting for connection...");

        Socket socket = server.accept();
        System.out.println("Client connected.");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String received = in.readLine();
        System.out.println("[Received] " + received);

        // Format: ENCRYPTED::a::b
        String[] parts = received.split("::");
        String encrypted = parts[0];
        int a = Integer.parseInt(parts[1]);
        int b = Integer.parseInt(parts[2]);

        String decrypted = affineDecrypt(encrypted, a, b);
        System.out.println("[Decrypted] " + decrypted.toUpperCase());  // final output: uppercase

        socket.close();
        server.close();
    }
}