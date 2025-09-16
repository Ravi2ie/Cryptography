import java.io.*;
import java.net.*;
import java.util.*;

public class ClientAutokey{
    public static String autokeyDecrypt(String plain,String key){
        StringBuilder res=new StringBuilder();
        int n=plain.length();
        int m=key.length();
       
        for(int i=0;i<m;i++){
            char p=plain.charAt(i);
            char k=key.charAt(i);
            int encryptedChar=(p-'a'+(k-'a'))%26;
            res.append((char)('A'+encryptedChar));
        }
        int j=0;
        for(int i=m;i<n;i++){
            char p=plain.charAt(i);
            char k=plain.charAt(j);
            j++;
            int encryptedChar=(p-'a'+(k-'a'))%26;
            res.append((char)('A'+encryptedChar));
        }
        return res.toString();
    }
    public static void main(String[] args)throws IOException{
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter the plain text:");
        String plain=scanner.nextLine();
        System.out.println("Enter the key:");
        String key=scanner.nextLine();

        String cipher=autokeyDecrypt(plain,key);
        System.out.println("[Plain Text]:"+plain);
        System.out.println("[Cipher Text]:"+cipher);

        Socket socket=new Socket("localhost",8080);
        BufferedWriter out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        out.write(cipher+"::"+key);
        out.flush();
        socket.close();
    }
}