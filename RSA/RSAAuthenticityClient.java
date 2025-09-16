import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class RSAAuthenticityClient {

    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("localhost", 9999);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Receive public key from server
            String pubKeyBase64 = in.readLine();
            PublicKey publicKey = getPublicKeyFromBase64(pubKeyBase64);
            System.out.println("Received public key from server");

            // Get message from user
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter message to send: ");
            String message = scanner.nextLine();

            // Send message to server
            out.println(message);

            // Receive signature from server
            String signatureBase64 = in.readLine();

            // Verify signature
            boolean verified = verify(message.getBytes(), Base64.getDecoder().decode(signatureBase64), publicKey);
            System.out.println("Signature valid? " + verified);
        }
    }

    private static PublicKey getPublicKeyFromBase64(String base64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    private static boolean verify(byte[] data, byte[] signature, PublicKey pubKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(pubKey);
        sig.update(data);
        return sig.verify(signature);
    }
}
