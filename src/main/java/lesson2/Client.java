package lesson2;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Client {

    public static void main(String[] args) {
        Client cl = new Client();
        try {
            cl.openConnection();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void openConnection() throws IOException {
        Socket socket = new Socket("localhost", 8089);
        File file = new File("111//photo_2021-11-19_15-43-21.jpg");
        sendName(socket, file);
        socket = new Socket("localhost", 8089);
        sendFile(socket, file);
    }

    private void sendName(Socket socket, File file) {
        try (DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {
            dataOutputStream.writeUTF(file.getName());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void sendFile(Socket socket, File file) {
        try {
            FileInputStream inFile = new FileInputStream(file);
            OutputStream out = socket.getOutputStream();
            int x;
            byte[] byteArray = new byte[1024];
            while ((x = inFile.read(byteArray)) != -1) {
                out.write(byteArray, 0, x);
            }
            System.out.println("Передано");
            Thread.sleep(1000);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
