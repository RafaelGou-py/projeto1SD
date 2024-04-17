import java.io.*;
import java.net.*;

public class ClienteArquivos{

    public static void main(String[] args) throws IOException{
        Socket clienteSocket = new Socket("localhost", 1212);
        DataInputStream dis = new DataInputStream(clienteSocket.getInputStream());
        DataOutputStream dos = new DataOutputStream(clienteSocket.getOutputStream());

        String fileName = "Arquivos/High noon Yone.jpg";                                             
        String operation = "DELETE";

        dos.writeUTF(fileName);
        dos.writeUTF(operation);

        switch (operation){
            case "UPLOAD":
                FileInputStream fis = new FileInputStream(fileName);
                byte[] buffer = new byte[4096];

                int read;
                while ((read = fis.read(buffer)) != -1){
                    dos.write(buffer, 0, read);
                }

                fis.close();
                System.out.println(dis.readUTF());
                break;

            case "DOWNLOAD":
                FileOutputStream fos = new FileOutputStream(fileName);
                buffer = new byte[4096];

                while ((read = dis.read(buffer)) != -1){
                    fos.write(buffer, 0, read);
                }

                fos.close();
                System.out.println(dis.readUTF());
                break;

            case "DELETE":
                System.out.println(dis.readUTF());
                break;
        }
        dis.close();
        dos.close();
        clienteSocket.close();
    }
}
