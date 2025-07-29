import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientCaesar {

    // Encrypt by shifting characters using custom a=0..z=25 logic
    static String caesarEncrypt(String text, int key) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            // Get 0-based index (a=0, ..., z=25), then apply shift
            int shifted = (c - 'a' + key) % 26;
            char encryptedChar = (char) ('a' + shifted);
            result.append(encryptedChar);
        }

        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter lowercase plaintext: ");
        String message = scanner.nextLine(); // only lowercase allowed

        System.out.print("Enter Caesar cipher key (integer): ");
        int key = scanner.nextInt();
        scanner.nextLine(); // flush newline

        String encrypted = caesarEncrypt(message, key);

        System.out.println("[Original]   " + message);
        System.out.println("[Encrypted]  " + encrypted);

        Socket socket = new Socket("localhost", 8080);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        out.write(encrypted + "::" + key + "\n");
        out.flush();

        socket.close();
    }
}