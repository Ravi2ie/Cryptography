import java.io.*;
import java.util.*;

public class ClientAffineAttack {

    static String affineDecrypt(String cipher, int a, int b) {
        StringBuilder result = new StringBuilder();
        int a_inv = modInverse(a, 26);
        if (a_inv == -1) return "";  

        for (char c : cipher.toCharArray()) {
            if (Character.isUpperCase(c)) {
                int y = c - 'A';
                int x = (a_inv * (y - b + 26)) % 26;
                result.append((char) ('a' + x));
            }
        }
        return result.toString();
    }

    static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) return x;
        }
        return -1;
    }

    static final int[] coprimes = {1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25};

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter UPPERCASE Affine Encrypted Cipher Text: ");
        String cipher = sc.nextLine();
        System.out.println("Enter the Plain text:");
        String plain=sc.nextLine();
        
        int found=-1;

        for (int a : coprimes) {
            for (int b = 0; b < 26; b++) {
                String decrypted = affineDecrypt(cipher, a, b);
                if(decrypted.equals(plain)){
                    System.out.println("Key pair: ("+a+","+b+")\nPlain Text "+plain);
                    found=1;
                    break;
                }
            }
        }
        if(found==-1){
            System.out.println("No key found");
        }
        sc.close();

    
    }
}