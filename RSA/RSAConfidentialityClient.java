package Cryptography.RSA;

import java.net.*;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.Cipher;
import java.util.Base64;

public class RSAConfidentialityClient {

    public static PublicKey getPublicKeyFromString(String keyStr) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static void main(String[] args) {
        try {
            // Generate client RSA key pair (optional, just for printing)
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair clientKeyPair = keyGen.generateKeyPair();

            PrivateKey clientPrivateKey = clientKeyPair.getPrivate();
            PublicKey clientPublicKey = clientKeyPair.getPublic();

            System.out.println("Client's RSA Key Pair:");
            System.out.println("Client Public Key (Base64):");
            System.out.println(Base64.getEncoder().encodeToString(clientPublicKey.getEncoded()));
            System.out.println();
            System.out.println("Client Private Key (Base64):");
            System.out.println(Base64.getEncoder().encodeToString(clientPrivateKey.getEncoded()));
            System.out.println();

            try (Socket socket = new Socket("localhost", 5000);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

                // Receive server's public key (Base64)
                String pubKeyStr = in.readLine();
                System.out.println("Received server's public key (Base64):");
                System.out.println(pubKeyStr);
                PublicKey serverPublicKey = getPublicKeyFromString(pubKeyStr);

                System.out.print("Enter message to encrypt and send: ");
                String message = userInput.readLine();
                System.out.println("Plaintext message:");
                System.out.println(message);

                // Encrypt message with server's public key
                String encryptedMessage = encrypt(message, serverPublicKey);
                System.out.println("Encrypted message (Base64):");
                System.out.println(encryptedMessage);

                out.println(encryptedMessage);
                System.out.println("Sent encrypted message to server.");

                // Read server confirmation
                String response = in.readLine();
                System.out.println("Server: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
