package lesson2.NIO;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class MyNioServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress(45001));
        serverChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Сервер стартовал.");

        while (true) {
            selector.select(); // Блокирующий вызов, только один
            for (SelectionKey event : selector.selectedKeys()) {
                if (event.isValid()) {
                    try {
                        if (event.isAcceptable()) { // Новое соединение
                            SocketChannel socketChannel = serverChannel.accept(); // Не блокирующий
                            socketChannel.configureBlocking(false);
                            System.out.println("Подключен " + socketChannel.getRemoteAddress());
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        } else if (event.isReadable()) { // Готов к чтению
                            SocketChannel socketChannel = (SocketChannel) event.channel();
                            handleReadable(socketChannel);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            selector.selectedKeys().clear();
        }
    }

    private static void handleReadable(SocketChannel socketChannel) throws IOException {
        ByteBuffer inboundBuffer = ByteBuffer.allocate(1024);
        int readBytes;
        RandomAccessFile fileForSend = new RandomAccessFile
                ("222//photo_2021-11-19_15-43-21.jpg", "rw");
        FileChannel fileChannel = fileForSend.getChannel();
        System.out.println("Начало закачки.");
        while ((readBytes = socketChannel.read(inboundBuffer)) > 0) {
            inboundBuffer.flip();
            if (inboundBuffer.hasRemaining()) {
                fileChannel.write(inboundBuffer);
                inboundBuffer.clear();
            }
        }
        System.out.println("Передача закончена.");
        if (readBytes == -1) {
            if (fileChannel != null) {
                fileChannel.close();
            }
            socketChannel.close();
        }
    }
}
