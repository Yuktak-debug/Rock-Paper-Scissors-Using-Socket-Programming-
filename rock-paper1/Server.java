import java.io.*;
import java.net.*;
import javax.swing.*;

public class Server {
    private static JFrame frame;
    private static JTextArea logArea;

    public static void main(String[] args) {
        createGUI();
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            logMessage("Server started. Waiting for players...");
            
            Socket player1 = serverSocket.accept();
            logMessage("Player 1 connected.");
            BufferedReader in1 = new BufferedReader(
                new InputStreamReader(player1.getInputStream()));
            PrintWriter out1 = new PrintWriter(player1.getOutputStream(), true);
            out1.println("Welcome Player 1! Waiting for Player 2...");

            Socket player2 = serverSocket.accept();
            logMessage("Player 2 connected.");
            BufferedReader in2 = new BufferedReader(
                new InputStreamReader(player2.getInputStream()));
            PrintWriter out2 = new PrintWriter(player2.getOutputStream(), true);
            out1.println("Player 2 has connected. Make your move (rock/paper/scissors):");
            out2.println("Welcome Player 2! Make your move (rock/paper/scissors):");

            String move1 = in1.readLine().toLowerCase().trim();
            String move2 = in2.readLine().toLowerCase().trim();
            String result = GameLogic.getResult(move1, move2);

            // Send results to both players
            out1.println("Player 1 chose: " + move1);
            out1.println("Player 2 chose: " + move2);
            out1.println("Result: " + result);
            out2.println("Player 1 chose: " + move1);
            out2.println("Player 2 chose: " + move2);
            out2.println("Result: " + result);

        } catch (IOException e) {
            logMessage("Server error: " + e.getMessage());
        }
    }

    private static void createGUI() {
        frame = new JFrame("Rock-Paper-Scissors Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        frame.add(new JScrollPane(logArea));
        
        frame.setVisible(true);
    }

    private static void logMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
        });
    }
}