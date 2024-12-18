import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private int n;
    private int r;
    private int l;

    private String[] playerPitch;
    private String playerTactic;

    private Cort cort;
    private Player player;
    private Dummy dummy;
    private Ball ball;

    public Main(int n, int r, int l, String[] playerPitch, String playerTactic) {
        this.n = n;
        this.r = r;
        this.l = l;
        this.playerPitch = playerPitch;
        this.playerTactic = playerTactic;
        this.cort = new Cort(n);
        this.player = new Player(r, l);
        this.dummy = new Dummy(r, l);
        this.ball = new Ball();
    }

    public boolean game() {
        int playerScore = 0;
        int dummyScore = 0;
        cort.startPositions(cort.getAllZones().get("A"), cort.getAllZones().get("D"), player, dummy);

        while (true) {

            player.zoneSelection(cort.getAllZones(), ball, playerPitch[0], playerTactic, dummy);


            while (true) {

                boolean flagDummy = dummy.move(ball);

                if (!flagDummy) {
                    playerScore++;

                    cort.startPositions(cort.getAllZones().get("A"), cort.getAllZones().get("D"), player, dummy);
                    break;
                }


                dummy.pitch(cort.getAllZones(), ball);
                boolean flagPlayer = player.move(ball);

                if (!flagPlayer) {
                    dummyScore++;

                    cort.startPositions(cort.getAllZones().get("A"), cort.getAllZones().get("D"), player, dummy);
                    break;
                }


                boolean miss = player.zoneSelection(cort.getAllZones(), ball, playerPitch[1], playerTactic, dummy); // subsequent serves have a chance to miss
                if (miss) {
                    dummyScore++;

                    cort.startPositions(cort.getAllZones().get("A"), cort.getAllZones().get("D"), player, dummy);
                    break;
                } else {

                }

            }


            if ((playerScore >= 10 || dummyScore >= 10) && Math.abs(playerScore - dummyScore) >= 2) {
                break;
            }

        }

        if (playerScore > dummyScore) {

            return true;
        } else {

            return false;
        }
    }

    public static void main(String[] args) {
        int[] nValues = {48, 192, 432, 768};
        int[] rValues = {1, 2, 3, 4, 5};
        int[] lValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        String[] playerPitch = {"first", "default"};
        String[] playerTactics = {"random", "far square"};

        for (int n : nValues) {

            int[][] playerWins = new int[rValues.length][lValues.length];

            for (int r = 0; r < rValues.length; r++) {
                for (int l = 0; l < lValues.length; l++) {
                    int playerTotalWin = 0;
                    int dummyTotalWin = 0;

                    for (int i = 0; i < 100; i++) {
                        int playerGameWin = 0;
                        int dummyGameWin = 0;
                        int playerSetWin = 0;
                        int dummySetWin = 0;

                        while (playerSetWin < 2 && dummySetWin < 2) {
                            playerGameWin = 0;
                            dummyGameWin = 0;

                            while (true) {
                                Main game = new Main(n, rValues[r], lValues[l], playerPitch, playerTactics[0]);
                                if (game.game()) {
                                    playerGameWin++;
                                } else {
                                    dummyGameWin++;
                                }

                                if ((playerGameWin >= 6 || dummyGameWin >= 6) && Math.abs(playerGameWin - dummyGameWin) >= 2) {
                                    break;
                                }
                            }


                            if (playerGameWin > dummyGameWin) {
                                playerSetWin++;
                            } else {
                                dummySetWin++;
                            }
                        }

                        if (playerSetWin > dummySetWin) {
                            System.out.println("Игрок победил!");
                            playerTotalWin++;
                        } else {
                            System.out.println("Болванчик победил!");
                            dummyTotalWin++;
                        }
                    }


                    playerWins[r][l] = playerTotalWin;
                }
            }


            System.out.println("Результаты для n = " + n);
            for (int r = 0; r < rValues.length; r++) {
                for (int l = 0; l < lValues.length; l++) {
                    System.out.println("r = " + rValues[r] + ", l = " + lValues[l] + ": " + playerWins[r][l]);
                }
            }


        }

        System.out.println("Закончили построение графиков для всех n значений.");
    }
}
