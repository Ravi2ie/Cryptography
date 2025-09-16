import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.util.Base64;

public class RSAAuthenticityServer {

    public static void main(String[] args) throws Exception {
        // Generate RSA key pair once on server start
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Print keys as Base64
        System.out.println("Generated RSA Key Pair:");
        System.out.println("Public Key (Base64):");
        System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println();
        System.out.println("Private Key (Base64):");
        System.out.println(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        System.out.println();

        try (ServerSocket serverSocket = new ServerSocket(9999);
             Socket socket = serverSocket.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Client connected.");

            // Send public key to client (Base64 encoded)
            String pubKeyEncoded = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            out.println(pubKeyEncoded);
            System.out.println("Sent public key to client");

            // Read message from client
            String message = in.readLine();
            System.out.println("Received message: " + message);

            // Server signs the message using its private key
            byte[] signature = sign(message.getBytes(), privateKey);
            String signatureBase64 = Base64.getEncoder().encodeToString(signature);

            // Send signature to client
            out.println(signatureBase64);
            System.out.println("Sent signature to client");

            System.out.println("Server finished processing.");
        }
    }

    private static byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(privateKey);
        sig.update(data);
        return sig.sign();
    }
}
