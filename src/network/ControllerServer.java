package src.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import src.config.serverConfig;
import src.input.InputHandler;
import src.model.ControllerCommand;
import src.util.logger;

public class ControllerServer implements Runnable {
    private DatagramSocket inputSocket;
    private final InputHandler inputHandler;

    public ControllerServer(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override
    public void run() {
        try {
            inputSocket = new DatagramSocket(
                serverConfig.DATA_PORT, 
                InetAddress.getByName(serverConfig.BIND_ADDRESS)
            );
            logger.log("Input  active on port: " + serverConfig.DATA_PORT);

            byte[] buffer = new byte[1024];
            while (serverConfig.isRunning) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                inputSocket.receive(packet); 

                String message = new String(packet.getData(), 0, packet.getLength()).trim();
                
               
                ControllerCommand command = new ControllerCommand(message);
                inputHandler.processCommand(command);
            }
        } catch (Exception e) {
            if (serverConfig.isRunning) {
                logger.log("Input Network Error: " + e.getMessage());
            }
        } finally {
            closeSocket();
        }
    }

    public void closeSocket() {
        if (inputSocket != null && !inputSocket.isClosed()) {
            inputSocket.close();
            logger.log("Input engine network closed safely.");
        }
    }
}