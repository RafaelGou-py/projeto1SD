import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ServerArquivos{

    public static void main(String[] args) throws IOException{
        ExecutorService executor = Executors.newFixedThreadPool(100); //Urilização de Threads.
        ServerSocket servidorSocket = new ServerSocket(1212); //Ligando o servidor na porta escolhida.
        System.out.println("Servidor ligado na porta 1212");

        while (true){
            Socket clienteSocket = servidorSocket.accept();
            executor.submit(new ClientHandler(clienteSocket));
        }
    }

    static class ClientHandler implements Runnable{
        private final Socket clienteSocket;

        public ClientHandler(Socket socket){
            this.clienteSocket = socket;
        }

        public void run(){
            try{
                DataInputStream dis = new DataInputStream(clienteSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(clienteSocket.getOutputStream());

                String fileName = dis.readUTF();
                String operation = dis.readUTF();

                switch (operation){
                    case "UPLOAD": //Coodigo da opção UPLOAD!
                        FileOutputStream fos = new FileOutputStream(fileName);
                        byte[] buffer = new byte[4096];

                        int filesize = 20000;
                        int read = 0;
                        int totalRead = 0;
                        int remaining = filesize;
                        while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0){
                            totalRead += read;
                            remaining -= read;
                            fos.write(buffer, 0, read);
                        }

                        fos.close();
                        dos.writeUTF("Arquivo: " + fileName + " ENVIADO corretamente.");
                        break;

                    case "DOWNLOAD": //Coodigo da opção DOWNLOAD!
                        FileInputStream fis = new FileInputStream(fileName);
                        buffer = new byte[4096];

                        while ((read = fis.read(buffer)) != -1){
                            dos.write(buffer, 0, read);
                        }

                        fis.close();
                        dos.writeUTF("Arquivo: " + fileName + " BAIXADO corretamente.");
                        break;

                    case "DELETE": //Coodigo da opção DELETE!
                        File file = new File(fileName);
                        if(file.delete()){
                            dos.writeUTF("Arquivo: " + fileName + " EXCLUIDO com SUCESSO!");
                        }else{
                            dos.writeUTF("Arquivo: " + fileName + " não encontrado."); //Caso o cliente passe um caminho que não existe,
                                                                                           //sera exibido essa mensagem!
                        }
                        break;
                }
                dis.close(); //Fechando o fluxo de Entrada
                dos.close(); //Fechando o fluxo de Saida
                clienteSocket.close();
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}
