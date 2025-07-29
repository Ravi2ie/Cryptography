import java.io.*;
import java.net.*;
import java.util.*;

public class ClientVignere{
    public static String VignereEncrypt(String plain,String key){
        StringBuilder res=new StringBuilder();

        for(int i=0;i<plain.length();i++){
            char p=plain.charAt(i);
            char k=key.charAt(i%key.length());
            int encryptedInd=(p-'a'+(k-'a'))%26;
            res.append((char)('A'+encryptedInd));
        }
        return res.toString();
    }
    public static void main(String[] args)throws IOException{
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter the PlainText:");
        String plain=scanner.nextLine();
        System.out.println("Enter the key:");
        String key=scanner.nextLine();

        String encrypted=VignereEncrypt(plain,key);
        System.out.println("[Plain Text]:"+plain);
        System.out.println("[Cipher Text]:"+encrypted);
        Socket socket=new Socket("localhost",8080);
        BufferedWriter out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        out.write(encrypted+"::"+key);
        out.flush();

        socket.close();
    }
}
