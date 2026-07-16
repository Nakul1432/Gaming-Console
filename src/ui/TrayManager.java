package src.ui;

import src.util.logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class TrayManager {

    private final Dashboard dashboard;
    private final Runnable shutdownCallback;
    private TrayIcon trayIcon;
    private boolean firstMinimize = true;

    public TrayManager(Dashboard dashboard, Runnable shutdownCallback) {
        this.dashboard = dashboard;
        this.shutdownCallback = shutdownCallback;
        setupTray();
    }

    private void setupTray() {

        if (!SystemTray.isSupported()) {
            logger.log("System tray is not supported on this system.");
            return;
        }

        try {

            SystemTray tray = SystemTray.getSystemTray();

           
            ImageIcon trayImage = new ImageIcon(getClass().getResource("/src/resources/favicon.png"));
            trayIcon = new TrayIcon(trayImage.getImage(), "Wireless Controller Server");

          

            PopupMenu popupMenu = new PopupMenu();

            MenuItem openItem = new MenuItem("Open Console");
            openItem.addActionListener(e -> dashboard.setVisible(true));

            MenuItem exitItem = new MenuItem("Shutdown");
            exitItem.addActionListener(e -> shutdownCallback.run());

            popupMenu.add(openItem);
            popupMenu.addSeparator();
            popupMenu.add(exitItem);

            trayIcon.setPopupMenu(popupMenu);

           
            trayIcon.addActionListener(e -> dashboard.setVisible(true));

            tray.add(trayIcon);

         
            dashboard.getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            dashboard.getFrame().addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {

                    Object[] options = {
                            "Minimize to Tray",
                            "Exit",
                            "Cancel"
                    };

                    int choice = JOptionPane.showOptionDialog(
                            dashboard.getFrame(),
                            "The controller server is still running.\n\nChoose what you want to do:",
                            "Exit Application",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );

                    switch (choice) {

                        case 0:
                            dashboard.setVisible(false);

                            if (firstMinimize) {
                                trayIcon.displayMessage(
                                        "Running in Background",
                                        "Wireless Controller Server is still active.",
                                        TrayIcon.MessageType.INFO
                                );
                                firstMinimize = false;
                            }
                            break;

                        case 1:
                            shutdownCallback.run();
                            break;

                        default:
                            // Cancel
                            break;
                    }
                }
            });

            logger.log("System tray initialized.");

        } catch (Exception ex) {
            logger.log("Failed to initialize system tray: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

 
    public void removeTrayIcon() {

        if (trayIcon != null) {
            SystemTray.getSystemTray().remove(trayIcon);
        }
    }
}