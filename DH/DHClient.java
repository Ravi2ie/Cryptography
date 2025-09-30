import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DHClient {

    private static long modExp(long base, long exp, long mod) {
        long result = 1;
        base = base % mod;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = (result * base) % mod;
            }
            exp >>= 1;
            base = (base * base) % mod;
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter User B's private key: ");
        long xb = sc.nextLong();
        sc.close();

        Socket socket = new Socket("localhost", 5000);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // receive q, a, ya from server
        String[] parts = in.readLine().split(",");
        long q = Long.parseLong(parts[0]);
        long a = Long.parseLong(parts[1]);
        long ya = Long.parseLong(parts[2]);

        System.out.println("Received parameters: q=" + q + ", a=" + a + ", ya=" + ya);

        // compute client public key and send it
        long yb = modExp(a, xb, q);
        out.println(yb);
        System.out.println("Sent client public key (yb): " + yb);

        // compute shared secret
        long kClient = modExp(ya, xb, q);
        System.out.println("Client computed secret key: " + kClient);

        socket.close();
    }
}
