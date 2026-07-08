import java.net.DatagramPacket;
import javax.swing.*;
import java.awt.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;



public class Main {

    private static JFrame frame ;
    private static JTextArea logArea;
    private static boolean isRunning = true;

    public static void main(String[] args){
        frame = new JFrame("Gamepad Server Dashboard");
        frame.setSize(500,300);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(25 , 25 ,25 ));
        logArea.setForeground(new Color(20,205,20));
        logArea.setFont(new Font("Consolas" , Font.PLAIN , 12));
        logArea.setMargin(new Insets(10,10,10,10));

        JScrollPane scrollPane = new JScrollPane(logArea);
        frame.add(scrollPane , BorderLayout.CENTER);

        log("Professional Gamepad Server Initializing...");

        setupSystemTray();
        
        startNetworkEngines();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private static void startNetworkEngines(){
         int dataPort = 5555;
         int discoveryPort = 5556;

         new Thread(() -> {
            try( DatagramSocket discoverySocket = new DatagramSocket(discoveryPort , InetAddress.getByName("0.0.0.0"))){
               discoverySocket.setBroadcast(true);
               log("📡 Handshake engine listening on port: " + discoveryPort);

               byte[] buffer = new byte[1024];
               while(isRunning){
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                discoverySocket.receive(packet);

                String message = new String(packet.getData() , 0 , packet.getLength()).trim();
                if (message.equals("GAME_CONTROLLER_DISCOVER")){
                    InetAddress phAddress = packet.getAddress();
                    int phPort = packet.getPort();
                    log("📱 Device Connected -> Link: " + phAddress.getHostAddress() + ":" + phPort);

                    String reply = "SERVER_HERE";
                    byte[] replyBuf = reply.getBytes();
                    DatagramPacket replyPacket = new DatagramPacket(replyBuf , replyBuf.length , phAddress , phPort);
                    discoverySocket.send(replyPacket);

                    DatagramPacket fallbackPacket = new DatagramPacket(replyBuf, replyBuf.length, InetAddress.getByName("255.255.255.255"), phPort);
                    discoverySocket.send(fallbackPacket);
                }
               }
            }
            catch(Exception e){
                log("Error: " + e.getMessage());
                e.printStackTrace();
            }
         }).start();

         new Thread(() -> {
            try{
                Robot robot = new Robot();
                DatagramSocket socket = new DatagramSocket(dataPort , InetAddress.getByName("0.0.0.0"));
                log("Controller input engine running on port: " + dataPort);

                byte[] buffer = new byte[1024];
                while(isRunning){
                    DatagramPacket packet = new DatagramPacket(buffer , buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData() , 0 , packet.getLength()).trim();
                    switch (message) {
                        case "BTN_UP":    robot.keyPress(KeyEvent.VK_UP);    robot.keyRelease(KeyEvent.VK_UP); break;
                        case "BTN_DOWN":  robot.keyPress(KeyEvent.VK_DOWN);  robot.keyRelease(KeyEvent.VK_DOWN); break;
                        case "BTN_LEFT":  robot.keyPress(KeyEvent.VK_LEFT);  robot.keyRelease(KeyEvent.VK_LEFT); break;
                        case "BTN_RIGHT": robot.keyPress(KeyEvent.VK_RIGHT); robot.keyRelease(KeyEvent.VK_RIGHT); break;
                        case "BTN_A":     robot.keyPress(KeyEvent.VK_Z);     robot.keyRelease(KeyEvent.VK_Z); break;
                        case "BTN_B":     robot.keyPress(KeyEvent.VK_X);     robot.keyRelease(KeyEvent.VK_X); break;
                        case "BTN_X":     robot.keyPress(KeyEvent.VK_C);     robot.keyRelease(KeyEvent.VK_C); break;
                        case "BTN_Y":     robot.keyPress(KeyEvent.VK_V);     robot.keyRelease(KeyEvent.VK_V); break;
                    }
                }
            }
            catch(Exception e){
                log("Input Error: " + e.getMessage());
                e.printStackTrace();
            }
         }).start();
    }

    private static void setupSystemTray(){
        if (!SystemTray.isSupported()) {
            log(" System tray is disabled or unsupported on this machine.");
            return;
        }
       try {
        SystemTray tray = SystemTray.getSystemTray();

        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(16, 16, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 16, 16);
        g.dispose();

        TrayIcon trayIcon = new TrayIcon(image ,"Wireless Controller Server" );
        trayIcon.setImageAutoSize(true);

        PopupMenu popup = new PopupMenu();
        MenuItem openItem = new MenuItem("Open Console");
        openItem.addActionListener(e ->{
            frame.setVisible(true);
        });

        MenuItem exitItem = new MenuItem("Shutdown");
        exitItem.addActionListener(e ->{
            isRunning = false;
            System.exit(0);

        });

            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);
            tray.add(trayIcon);

        frame.addWindowListener(new WindowAdapter() 
             {
            @Override
            public void windowClosing(WindowEvent e){
                frame.setVisible(false);
                trayIcon.displayMessage("Running in Background", "Your controller server is still active right here.", TrayIcon.MessageType.INFO);
            }

        });
            
        } catch (Exception e) {
           System.err.println("Could not establish Tray Anchor: " + e.getMessage());
        }
    }

    public static void log(String text) {
      
        SwingUtilities.invokeLater(() -> logArea.append(text + "\n"));
    }
}