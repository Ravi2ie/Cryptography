import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerAES {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("AES Server started. Waiting for client...");

        Socket socket = serverSocket.accept();
        System.out.println("Client connected.");

        DataInputStream in = new DataInputStream(socket.getInputStream());
        String cipherHex = in.readUTF();
        System.out.println("Received Ciphertext: " + cipherHex);

        // Same AES-128 key as client
        String keyHex = "2B7E151628AED2A6ABF7158809CF4F3C";
        byte[] key = AESUtil.fromHex(keyHex);

        // Decrypt and print
        byte[] plaintext = AESUtil.decrypt(AESUtil.fromHex(cipherHex), key);
        System.out.println("Decrypted Plaintext: " + new String(plaintext, StandardCharsets.UTF_8));

        socket.close();
        serverSocket.close();
    }
}
