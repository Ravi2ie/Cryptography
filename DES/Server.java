import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server started. Waiting for client...");

        Socket socket = serverSocket.accept();
        System.out.println("Client connected.");

        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Read ciphertext from client
        String cipherHex = in.readUTF();
        System.out.println("Received Ciphertext: " + cipherHex);

        // Convert back to bytes
        int len = cipherHex.length();
        byte[] cipherBytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            cipherBytes[i / 2] = (byte) ((Character.digit(cipherHex.charAt(i), 16) << 4)
                                       + Character.digit(cipherHex.charAt(i+1), 16));
        }

        // Known DES key
        long key = 0x133457799BBCDFF1L;

        // Decrypt
        byte[] plainBytes = DES.decrypt(cipherBytes, key);
        String plainText = new String(plainBytes, StandardCharsets.UTF_8);

        System.out.println("Decrypted Plaintext: " + plainText);

        socket.close();
        serverSocket.close();
    }
}
