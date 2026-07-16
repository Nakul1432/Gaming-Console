package src.ui;

import javax.swing.*;
import java.awt.*;
import src.util.logger;

import java.net.URL;

public class Dashboard {
    private final JFrame frame;
    private final JTextArea logArea;

    public Dashboard() {
        frame = new JFrame("Gamepad Server Dashboard");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        URL iconURL = getClass().getResource("/src/resources/server_icon.png");
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            frame.setIconImage(icon.getImage());
        }

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(25, 25, 25));
        logArea.setForeground(new Color(20, 205, 20));
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(logArea);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);

        
        logger.register(text -> SwingUtilities.invokeLater(() -> logArea.append(text + "\n")));
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public JFrame getFrame() {
        return this.frame;
    }
}