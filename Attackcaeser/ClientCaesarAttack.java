import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientCaesarAttack {

    // Decrypt by shifting characters back
    static String caesarDecrypt(String cipher, int key) {
        StringBuilder result = new StringBuilder();

        for (char c : cipher.toCharArray()) {
            if (Character.isLetter(c)) {
                int shifted = (c - 'A' - key + 26) % 26;
                char decryptedChar = (char) ('a' + shifted);
                result.append(decryptedChar);
            } else {
                result.append(c); // Keep non-alphabet characters as-is
            }
        }

        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Caesar encrypted text (UPPERCASE): ");
        String cipherText = scanner.nextLine();  // e.g., "KHOOR"

        System.out.println("\n[Brute Force Attack Results]:");

        // Try all 26 keys and print each guess
        for (int key = 0; key < 26; key++) {
            String guess = caesarDecrypt(cipherText, key);
            System.out.printf("Key %2d: %s\n", key, guess);
        }

        // Optionally, send all guesses to server for logging or checking
        Socket socket = new Socket("localhost", 8030);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        out.write("Brute Force Caesar Results:\n");
        for (int key = 0; key < 26; key++) {
            String guess = caesarDecrypt(cipherText, key);
            out.write("Key " + key + ": " + guess + "\n");
        }

        out.flush();
        socket.close();
    }
}