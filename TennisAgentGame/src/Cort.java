import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Cort {
    private int[][] matrix;
    private Map<String, Zone> allZones;

    public Cort(int numberOfSquares) {
        int width = (int) Math.sqrt(numberOfSquares / 3);
        this.matrix = new int[width][width * 3];

        // Creating the matrix 
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = 0;
            }
        }

        this.allZones = divisionIntoZones(width * 3, width);
    }

    private Map<String, Zone> divisionIntoZones(int length, int width) {
        Zone a = new Zone(length, width, "A");
        Zone b = new Zone(length, width, "B");
        Zone c = new Zone(length, width, "C");
        Zone d = new Zone(length, width, "D");
        Zone e = new Zone(length, width, "E");
        Zone f = new Zone(length, width, "F");

        Map<String, Zone> zones = new HashMap<>();
        zones.put("A", a);
        zones.put("B", b);
        zones.put("C", c);
        zones.put("D", d);
        zones.put("E", e);
        zones.put("F", f);

        return zones;
    }

    public void startPositions(Zone a, Zone d, Player player, Dummy dummy) {
        int playerX = Math.round((a.getMinX() + a.getMaxX()) / 2);
        int playerY = Math.round((a.getMinY() + a.getMaxY()) / 2);
        player.setX(playerX);
        player.setY(playerY);

        int dummyX = d.getMinX();
        int dummyY = Math.round((d.getMinY() + d.getMaxY()) / 2);
        dummy.setX(dummyX);
        dummy.setY(dummyY);
    }

    public void printCortState(Player player, Dummy dummy, Ball ball) {
        System.out.println("Текущее состояние корта");

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (i == player.getY() && j == player.getX()) {
                    System.out.print("P ");
                } else if (i == dummy.getY() && j == dummy.getX()) {
                    System.out.print("D ");
                } else if (i == ball.getY() && j == ball.getX()) {
                    System.out.print("B ");
                } else {
                    System.out.print("O ");
                }
            }
            System.out.println();
        }
    }

    public Map<String, Zone> getAllZones() {
        return allZones;
    }
}