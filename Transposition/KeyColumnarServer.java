import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) {
        int port = 12345;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started, waiting for client...");

            try (
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            ) {
                System.out.println("Client connected.");

                // Read plaintext and key
                String plaintext = in.readLine();
                String key = in.readLine();

                System.out.println("Received plaintext: " + plaintext);
                System.out.println("Received key: " + key);

                // Encrypt using Key Columnar Transposition cipher
                String ciphertext = encrypt(plaintext, key);

                // Send ciphertext back to client
                out.println(ciphertext);

                System.out.println("Sent ciphertext: " + ciphertext);
                System.out.println("Server closing connection.");
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    // Encryption method
    private static String encrypt(String plaintext, String key) {
        plaintext = plaintext.replaceAll("\\s", ""); // remove spaces
        int cols = key.length();
        int rows = (int) Math.ceil((double) plaintext.length() / cols);

        char[][] matrix = new char[rows][cols];
        int idx = 0;

        // Fill matrix row-wise with plaintext chars, pad with 'X'
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (idx < plaintext.length()) {
                    matrix[r][c] = plaintext.charAt(idx++);
                } else {
                    matrix[r][c] = 'X';
                }
            }
        }

        // Sort key characters to get column reading order
        Character[] keyChars = new Character[cols];
        for (int i = 0; i < cols; i++) {
            keyChars[i] = key.charAt(i);
        }

        Character[] sortedKeyChars = keyChars.clone();
        Arrays.sort(sortedKeyChars);

        StringBuilder ciphertext = new StringBuilder();

        // For each letter in sorted key, find the corresponding column and read down
        boolean[] used = new boolean[cols];
        for (char ch : sortedKeyChars) {
            for (int i = 0; i < cols; i++) {
                if (!used[i] && keyChars[i] == ch) {
                    for (int r = 0; r < rows; r++) {
                        ciphertext.append(matrix[r][i]);
                    }
                    used[i] = true; // mark column used
                    break;
                }
            }
        }

        return ciphertext.toString();
    }
}
