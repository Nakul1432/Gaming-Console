package src;

import src.config.serverConfig;
import src.input.InputHandler;
import src.network.ControllerServer;
import src.network.DiscoveryServer;
import src.ui.Dashboard;
import src.ui.TrayManager;
import src.util.logger;
import javax.swing.SwingUtilities;

public class Main {
    private static DiscoveryServer discoveryServer;
    private static ControllerServer controllerServer;
    private static TrayManager trayManager;

    public static void main(String[] args) {
       
        SwingUtilities.invokeLater(() -> {
         
            serverConfig.isRunning = true;
            Dashboard dashboard = new Dashboard();
            InputHandler inputHandler = new InputHandler();

           
            trayManager = new TrayManager(dashboard, Main::shutdownSystemCleanly);

        
            discoveryServer = new DiscoveryServer();
            controllerServer = new ControllerServer(inputHandler);

            new Thread(discoveryServer, "Discovery-Thread").start();
            new Thread(controllerServer, "Controller-Thread").start();

        
            dashboard.getFrame().setSize(500, 300);
            dashboard.getFrame().setLocationRelativeTo(null); 
            dashboard.getFrame().setVisible(true);
            
            logger.log("All system modules are online and focused!");
        });
    }

    private static void shutdownSystemCleanly() {
        logger.log("Stopping active background services...");
        serverConfig.isRunning = false;

       
        if (discoveryServer != null) {
            discoveryServer.closeSocket();
        }
        if (controllerServer != null) {
            controllerServer.closeSocket();
        }

       
        if (trayManager != null) {
            trayManager.removeTrayIcon(); 
        }

        logger.log("Server application terminated safely.");
        System.exit(0);
    }
}