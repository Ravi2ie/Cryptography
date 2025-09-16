package Cryptography.RSA;

import java.net.*;
import java.io.*;
import java.security.*;
import javax.crypto.Cipher;
import java.util.Base64;

public class RSAConfidentialityServer {
    private static KeyPair keyPair;

    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(bytes);
        return new String(decryptedBytes);
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started. Waiting for client...");

            keyPair = generateRSAKeyPair();

            // Print the keys involved
            System.out.println("Generated RSA Key Pair:");
            System.out.println("Public Key (Base64):");
            System.out.println(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
            System.out.println();
            System.out.println("Private Key (Base64):");
            System.out.println(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
            System.out.println();

            try (Socket clientSocket = serverSocket.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                // Send public key to client (encoded in Base64)
                byte[] pubKeyBytes = keyPair.getPublic().getEncoded();
                String pubKeyStr = Base64.getEncoder().encodeToString(pubKeyBytes);
                out.println(pubKeyStr);

                // Receive encrypted message
                String encryptedMessage = in.readLine();
                System.out.println("Received encrypted message: " + encryptedMessage);

                // Decrypt
                String decryptedMessage = decrypt(encryptedMessage, keyPair.getPrivate());
                System.out.println("Decrypted message: " + decryptedMessage);

                out.println("Message received and decrypted successfully.");
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
