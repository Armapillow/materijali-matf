package TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    enum Players {
        PLAYER_X,
        PLAYER_O
    };

    public static void main(String[] args) {
        int port = 12345;

        String[][] tabla = new String[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                tabla[i][j] = "-";

        try (ServerSocket server = new ServerSocket(port)) {
            System.err.println("Server started on port " + port);

            while (true) {
                System.err.println("Waiting for players");
                // Player 1
                Socket playerX = server.accept();
                System.err.println("Accepted player X");
                var inXPlayer = new BufferedReader(new InputStreamReader(playerX.getInputStream()));
                var outXPlayer = new BufferedWriter(new OutputStreamWriter(playerX.getOutputStream()));
                writeToClient(outXPlayer, "Vi ste igrac X");
                writeToClient(outXPlayer, getCurrentStateOfTable(tabla));


                // Player 2
                Socket playerO = server.accept();
                System.err.println("Accepted Player O");
                var inOPlayer = new BufferedReader(new InputStreamReader(playerO.getInputStream()));
                var outOPlayer = new BufferedWriter(new OutputStreamWriter(playerO.getOutputStream()));
                writeToClient(outOPlayer, "Vi ste igrac O");


                while (true) {
                    // move from playerX
                    // TODO: nevalidan potez
                    int moveX = Integer.parseInt(inXPlayer.readLine().trim());
                    playMove(tabla, moveX, Players.PLAYER_X);
                    writeToClient(outXPlayer, getCurrentStateOfTable(tabla));
                    writeToClient(outOPlayer, getCurrentStateOfTable(tabla));


                    // TODO: dodaj za nereseno
                    if (isWinnerX(moveX, tabla)) {
                        writeToClient(outXPlayer, "Pobedili ste!");
                        writeToClient(outOPlayer, "Pobedio je igrac X");
                        break;
                    }

                    // move from playerO
                    // TODO: nevalidan potez
                    int moveO = Integer.parseInt(inOPlayer.readLine().trim());
                    playMove(tabla, moveO, Players.PLAYER_O);
                    writeToClient(outOPlayer, getCurrentStateOfTable(tabla));
                    writeToClient(outXPlayer, getCurrentStateOfTable(tabla));

                    // TODO: dodaj za nereseno
                    if (isWinnerO(moveO, tabla)) {
                        writeToClient(outOPlayer, "Pobedili ste!");
                        writeToClient(outXPlayer, "Pobedio je igrac O");
                        break;
                    }

                }


                inOPlayer.close();
                outOPlayer.close();
                inXPlayer.close();
                outXPlayer.close();
            }

        } catch (IOException ex) {
            System.err.println("Server error!");
            System.err.println(ex.getMessage());
        }
    }

    private static void writeToClient(BufferedWriter out, String mess) throws IOException {
        out.write(mess);
        out.newLine();
        out.flush();
    }
    private static boolean isWinnerX(int moveX, String[][] tabla) {
        int x = (moveX - 1) / 3;
        int y = (moveX - 1) % 3;

        for (int i = 0; i < 3; i++) {
            if (!tabla[x][i].equalsIgnoreCase("X"))
                break;
            if (i == 2)
                return true;
        }

        for (int i = 0; i < 3; i++) {
            if (!tabla[i][y].equalsIgnoreCase("X"))
                break;
            if (i == 2)
                return true;
        }

        if (x == y) {
            for (int i = 0; i < 3; i++) {
                if (!tabla[i][i].equalsIgnoreCase("X"))
                    break;
                if (i == 2)
                    return true;
            }
        }

        if (x + y == 2) {
            for (int i = 0; i < 3; i++) {
                if (!tabla[i][2-i].equalsIgnoreCase("X"))
                    break;
                if (i == 2)
                    return true;
            }
        }


        return false;
    }

    private static boolean isWinnerO(int moveO, String[][] tabla) {
        int x = (moveO - 1) / 3;
        int y = (moveO - 1) % 3;

        for (int i = 0; i < 3; i++) {
            if (!tabla[x][i].equalsIgnoreCase("O"))
                break;
            if (i == 2)
                return true;
        }

        for (int i = 0; i < 3; i++) {
            if (!tabla[i][y].equalsIgnoreCase("O"))
                break;
            if (i == 2)
                return true;
        }

        if (x == y) {
            for (int i = 0; i < 3; i++) {
                if (!tabla[i][i].equalsIgnoreCase("O"))
                    break;
                if (i == 2)
                    return true;
            }
        }

        if (x + y == 2) {
            for (int i = 0; i < 3; i++) {
                if (!tabla[i][2-i].equalsIgnoreCase("O"))
                    break;
                if (i == 2)
                    return true;
            }
        }


        return false;
    }

    private static void playMove(String[][] tabla, int moveX, Players players) {
        int i = (moveX - 1) / 3;
        int j = (moveX - 1) % 3;

        if (players == Players.PLAYER_X) {
            tabla[i][j] = "X";
        } else if (players == Players.PLAYER_O) {
            tabla[i][j] = "O";
        }


    }

    private static String getCurrentStateOfTable(String[][] table) {
        int n = table.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(table[i][j]);
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
