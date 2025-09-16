import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientAffine {

    static String affineEncrypt(String text, int a, int b) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                int x = c - 'a'; // Convert to 0–25
                int y = (a * x + b) % 26;
                result.append((char)(y + 'A')); // Convert to uppercase
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter plaintext (lowercase letters only): ");
        String message = scanner.nextLine();

        System.out.print("Enter key a (coprime with 26): ");
        int a = scanner.nextInt();

        System.out.print("Enter key b (integer): ");
        int b = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        String encrypted = affineEncrypt(message, a, b);
        System.out.println("[Original] " + message);
        System.out.println("[Encrypted] " + encrypted);

        Socket socket = new Socket("localhost", 8080);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // Send encrypted::a::b
        out.write(encrypted + "::" + a + "::" + b + "\n");
        out.flush();

        socket.close();
    }
}