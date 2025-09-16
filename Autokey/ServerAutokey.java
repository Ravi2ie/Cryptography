import java.io.*;
import java.net.*;
import java.util.*;

public class ServerAutoKey{
    public static String autokeyDecrypt(String cipher,String key){
        int m=key.length();
        int n=cipher.length();
        StringBuilder res=new StringBuilder();

        for(int i=0;i<m;i++){
            char c=cipher.charAt(i);
            char k=key.charAt(i);
            int decryptedChar=(c-'A'-(k-'a')+26)%26;
            res.append((char)('a'+decryptedChar));
        }
        int j=0;
        for(int i=m;i<n;i++){
            char c=cipher.charAt(i);
            char k=res.charAt(j);
            j++;
            int decryptedChar=(c-'A'-(k-'a')+26)%26;
            res.append((char)('a'+decryptedChar));
        }
        return res.toString();
    }
    public static void main(String[] args)throws IOException{
        ServerSocket ss=new ServerSocket(8080);
        System.out.println("Waiting for connection");
        Socket socket=ss.accept();
        System.out.println("Client Connected");
        BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String received=br.readLine();
        String[] parts=received.split("::");
        String cipher=parts[0];
        String key=parts[1];
        String plain=autokeyDecrypt(cipher,key);
        System.out.println("[Cipher Text]:"+cipher);
        System.out.println("[Plain Text]:"+plain);
        socket.close();
    }
}