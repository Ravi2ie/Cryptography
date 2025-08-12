import java.io.*;
import java.util.Scanner;

public class ClientCaesarAttack {

    static String caesarDecrypt(String cipher, int key) {
        StringBuilder result = new StringBuilder();

        for (char c : cipher.toCharArray()) {
            if (Character.isLetter(c)) {
                int shifted = (c - 'A' - key + 26) % 26;
                char decryptedChar = (char) ('a' + shifted);
                result.append(decryptedChar);
            } else {
                result.append(c); 
            }
        }

        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Caesar encrypted text (UPPERCASE): ");
        String cipherText = scanner.nextLine();  

        System.out.println("Enter the Plain text:");
        String plain=scanner.nextLine();

        System.out.println("\n[Brute Force Attack Results]:");
        int found=-1;
        for (int key = 0; key < 26; key++) {
            String guess = caesarDecrypt(cipherText, key);
            if(guess.equals(plain)){
                found=1;
                System.out.println("[Key] "+key+"\nPlain Text " + plain);
                break;
            }
        }
        if(found==-1){
            System.out.println("No valid key found");
        }

        scanner.close();

    }
}