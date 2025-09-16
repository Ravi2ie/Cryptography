import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientAES {
    public static void main(String[] args) throws Exception {
        // Connect to server
        Socket socket = new Socket("localhost", 5000);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Get plaintext
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter plaintext: ");
        String msg = sc.nextLine();

        // AES-128 key (example: NIST SP 800-38A style test vector)
        String keyHex = "2B7E151628AED2A6ABF7158809CF4F3C";
        byte[] key = AESUtil.fromHex(keyHex);

        // Print round keys
        System.out.println("\nAES-128 Round Keys (K0..K10):");
        String[] rounds = AESUtil.getRoundKeysHex(key);
        for (int i = 0; i < rounds.length; i++) {
            System.out.printf("K%-2d: %s%n", i, rounds[i]); // K0 is original key, K10 last
        }

        // Encrypt
        byte[] cipher = AESUtil.encrypt(msg.getBytes(StandardCharsets.UTF_8), key);
        String cipherHex = AESUtil.toHex(cipher);
        System.out.println("\nCiphertext: " + cipherHex);

        // Send ciphertext to server
        out.writeUTF(cipherHex);
        out.flush();
        System.out.println("Ciphertext sent to server.");

        socket.close();
    }
}
