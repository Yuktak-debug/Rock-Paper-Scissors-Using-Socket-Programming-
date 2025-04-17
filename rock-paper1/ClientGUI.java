import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ClientGUI {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private PrintWriter out;
    private String playerName;

    public ClientGUI() {
        createGUI();
        connectToServer();
    }

    private void createGUI() {
        // Create main frame
        frame = new JFrame("Rock-Paper-Scissors");  // Fixed: Removed title:
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Fixed: Correct constant name
        frame.setSize(600, 400);  // Fixed: Removed width: and height:

        // Create game buttons
        JButton rockBtn = new JButton("Rock");
        JButton paperBtn = new JButton("Paper");
        JButton scissorsBtn = new JButton("Scissors");

        // Add button actions
        rockBtn.addActionListener(e -> sendMove("rock"));
        paperBtn.addActionListener(e -> sendMove("paper"));
        scissorsBtn.addActionListener(e -> sendMove("scissors"));

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(rockBtn);
        buttonPanel.add(paperBtn);
        buttonPanel.add(scissorsBtn);

        // Create chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        // Create input field
        inputField = new JTextField();
        inputField.addActionListener(e -> {
            sendMessage(inputField.getText());
            inputField.setText("");
        });

        // Layout components
        frame.setLayout(new BorderLayout());
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputField, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> {
                try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))) {
                    String message;
                    while ((message = in.readLine()) != null) {
                        appendToChat(message);
                    }
                } catch (IOException e) {
                    appendToChat("Connection error: " + e.getMessage());
                }
            }).start();
        } catch (IOException e) {
            appendToChat("Failed to connect to server: " + e.getMessage());
        }
    }

    private void sendMove(String move) {
        out.println(move);
        appendToChat("You chose: " + move);
    }

    private void sendMessage(String message) {
        out.println(message);
        appendToChat("You: " + message);
    }

    private void appendToChat(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientGUI());
    }
}