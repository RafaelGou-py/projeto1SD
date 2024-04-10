import java.io.*;
import java.net.*;

public class ClienteArquivos{

    public static void main(String[] args) throws IOException{
        Socket clienteSocket = new Socket("localhost", 1212); //Se conectando ao servidor.
        DataInputStream dis = new DataInputStream(clienteSocket.getInputStream());
        DataOutputStream dos = new DataOutputStream(clienteSocket.getOutputStream());

        String fileName = "Arquivos/High noon Yone.jpg"; //Caminho do arquivo(NA PASTA ARQUIVOS do codigo) que vc deseja manipular.
                                                  //Caso a operação abaixo seja "DOWNLOAD", o arquivo nao deve existir na pasta.
        String operation = "DELETE"; //Trocar para o que vc quer que aconteça com o arquivo.

        dos.writeUTF(fileName);
        dos.writeUTF(operation);

        switch (operation){
            case "UPLOAD": //Infelizmente não está funcionando corretamente.
                FileInputStream fis = new FileInputStream(fileName);
                byte[] buffer = new byte[4096];

                int read;
                while ((read = fis.read(buffer)) != -1){
                    dos.write(buffer, 0, read);
                }

                fis.close();
                System.out.println(dis.readUTF());
                break;

            case "DOWNLOAD": //Vai baixar o arquivo no diretorio que vc selecionar.
                FileOutputStream fos = new FileOutputStream(fileName);
                buffer = new byte[4096];

                while ((read = dis.read(buffer)) != -1){
                    fos.write(buffer, 0, read);
                }

                fos.close();
                System.out.println(dis.readUTF());
                break;

            case "DELETE": //Vai deletar o arquivo que vc selecionar.
                System.out.println(dis.readUTF());
                break;
        }
        dis.close(); //Fechando o fluxo de Entrada
        dos.close(); //Fechando o fluxo de Saida
        clienteSocket.close();
    }
}