package lesson2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class IOServer {

    public static void main(String[] args) {

        IOServer ser = new IOServer();
        ser.openConnection();
    }

    private void openConnection() {
        try (ServerSocket serverSocket = new ServerSocket(8089)) {
            System.out.println("Сервер ожидает подключения... ");
            Socket socket = serverSocket.accept();
            System.out.println("Клиент подключился! " + socket.getRemoteSocketAddress());
            File file = openFile(socket);
            socket = serverSocket.accept();
            System.out.println("Клиент подключился! " + socket.getRemoteSocketAddress());
            trans(socket, file);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void trans(Socket socket, File file) {
        try (InputStream in = socket.getInputStream();
             FileOutputStream outFile = new FileOutputStream(file)) {
            int x;
            byte[] bytes = new byte[1024];
            System.out.println("Передача начата.");
            while ((x = in.read(bytes)) != -1) {
                outFile.write(bytes, 0, x);
            }
            System.out.println("Передача закончена успешно.");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private File openFile(Socket socket) {
        try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())){
            return new File("222//" + dataInputStream.readUTF());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }
}




