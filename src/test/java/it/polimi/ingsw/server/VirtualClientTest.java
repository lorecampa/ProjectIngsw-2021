package it.polimi.ingsw.server;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VirtualClientTest {
    static ServerSocket serverSocket;
    static Socket clientSocket;
    static final int PORT = 2020;




    @Test
    @Order(1)
    static void initServer() throws IOException {
        new Thread(()-> {
            try {
                new ServerSocket(PORT).accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        serverSocket = new ServerSocket(PORT);


    }


    @Test
    @Order(2)
    static void initClient() throws IOException {
        clientSocket = new Socket("localhost", PORT);

    }

    @Test
    @Order(3)
    void currentPlayerChange() {
    }
}