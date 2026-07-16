package src.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import src.config.serverConfig;
import src.util.logger;

public class DiscoveryServer implements Runnable {
    private DatagramSocket discoverySocket;

    @Override
    public void run() {
        try {
            discoverySocket = new DatagramSocket(
                serverConfig.DISCOVERY_PORT, 
                InetAddress.getByName(serverConfig.BIND_ADDRESS)
            );
            discoverySocket.setBroadcast(true);
            logger.log("📡 Handshake engine active on port: " + serverConfig.DISCOVERY_PORT);

            byte[] buffer = new byte[1024];
            while (serverConfig.isRunning) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                discoverySocket.receive(packet); 

                String message = new String(packet.getData(), 0, packet.getLength()).trim();
                if (message.equals(serverConfig.DISCOVERY_SIGNAL)) {
                    InetAddress phoneAddress = packet.getAddress();
                    int phonePort = packet.getPort();
                    logger.log("📱 Handshake matching -> Device: " + phoneAddress.getHostAddress() + ":" + phonePort);

                    String replyText = serverConfig.SERVER_RESPONSE;
                    byte[] replyBuf = replyText.getBytes();
                    
                    
                    DatagramPacket replyPacket = new DatagramPacket(replyBuf, replyBuf.length, phoneAddress, phonePort);
                    discoverySocket.send(replyPacket);

                   
                    DatagramPacket fallbackPacket = new DatagramPacket(
                        replyBuf, replyBuf.length, 
                        InetAddress.getByName(serverConfig.BROADCAST_ADDRESS), 
                        phonePort
                    );
                    discoverySocket.send(fallbackPacket);
                }
            }
        } catch (Exception e) {
            if (serverConfig.isRunning) {
                logger.log("Network Error: " + e.getMessage());
            }
        } finally {
            closeSocket();
        }
    }

    public void closeSocket() {
        if (discoverySocket != null && !discoverySocket.isClosed()) {
            discoverySocket.close();
            logger.log(" Handshake engine socket closed .");
        }
    }
}