import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 12345;

        try (
            Socket socket = new Socket(serverAddress, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
        ) {
            System.out.println("Connected to server.");

            System.out.print("Enter plaintext: ");
            String plaintext = scanner.nextLine();

            System.out.print("Enter key: ");
            String key = scanner.nextLine();

            // Send plaintext and key
            out.println(plaintext);
            out.println(key);

            // Receive ciphertext
            String ciphertext = in.readLine();
            System.out.println("Ciphertext from server: " + ciphertext);

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
