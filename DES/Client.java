import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        // Connect to server
        Socket socket = new Socket("localhost", 5000);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Input plaintext
        System.out.print("Enter plaintext: ");
        String msg = sc.nextLine();

        // Known DES key
        long key = 0x133457799BBCDFF1L;

        // Generate & print subkeys
        long[] rk = DES.getSubkeys(key);
        System.out.println("\nGenerated 16 Subkeys:");
        for (int i = 0; i < 16; i++) {
            System.out.printf("K%-2d: %012X\n", i + 1, rk[i]);
        }

        // Encrypt
        byte[] plaintext = msg.getBytes(StandardCharsets.UTF_8);
        byte[] cipherBytes = DES.encrypt(plaintext, key);
        String cipherHex = DES.toHex(cipherBytes);

        System.out.println("\nCiphertext: " + cipherHex);

        // Send ciphertext to server
        out.writeUTF(cipherHex);
        out.flush();

        System.out.println("\nCiphertext sent to server.");
        socket.close();
    }
}
