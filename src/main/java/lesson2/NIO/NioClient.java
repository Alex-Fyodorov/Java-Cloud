package lesson2.NIO;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NioClient {

    public static void main(String[] args) {
        try {
            InetSocketAddress address = new InetSocketAddress("localhost", 45001);
            SocketChannel socketChannel = SocketChannel.open(address);
            System.out.println("Connecting to Server on port 1111...");
            RandomAccessFile file = new RandomAccessFile(
                    "111//photo_2021-11-19_15-43-21.jpg", "r");
            FileChannel inFile = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = inFile.read(buffer);
            System.out.println("Передача началась.");
            while (bytesRead > 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer);
                }
                buffer.clear();
                bytesRead = inFile.read(buffer);
            }
            System.out.println("Файл передан.");
            file.close();
            inFile.close();
            socketChannel.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
