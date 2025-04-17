public class GameLogic {
    public static String getResult(String p1, String p2) {
        if (!isValidMove(p1) || !isValidMove(p2)) {
            return "Invalid move detected!";
        }
        if (p1.equals(p2)) return "Draw!";
        if ((p1.equals("rock") && p2.equals("scissors")) ||
            (p1.equals("scissors") && p2.equals("paper")) ||
            (p1.equals("paper") && p2.equals("rock"))) {
            return "Player 1 wins!";
        }
        return "Player 2 wins!";
    }
    
    private static boolean isValidMove(String move) {
        return move != null && 
               (move.equals("rock") || move.equals("paper") || move.equals("scissors"));
    }
}