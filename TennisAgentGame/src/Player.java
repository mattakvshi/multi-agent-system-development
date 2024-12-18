import java.util.Map;
import java.util.Random;

public class Player {
    private int r; // radius of view
    private int l; // maximum distance of movement between attacks
    private Integer x; // x-coordinate of the player
    private Integer y; // y-coordinate of the player
    private int score; // score of the player
    private int game; // game number
    private int set; // set number
    private int match; // match number

    public Player(int r, int l) {
        this.r = 2 * r;
        this.l = l;
        this.x = null; // no coordinates at initialization
        this.y = null;
        this.score = 0;
        this.game = 0;
        this.set = 0;
        this.match = 0;
    }

    public boolean pitch(Zone zone, Ball ball, String message) {
        Random random = new Random();
        int squareX = random.nextInt(zone.getMaxX() - zone.getMinX() + 1) + zone.getMinX();
        int squareY = random.nextInt(zone.getMaxY() - zone.getMinY() + 1) + zone.getMinY();
        if (message.equals("Не промах")) {
            ball.newLocation(squareX, squareY);
            // print("Player sent the ball to square (" + ball.getX() + ", " + ball.getY() + ") in zone " + zone.getName());
            return false;
        } else {
            int squareOfMissX = random.nextInt(3) - 1 + squareX;
            int squareOfMissY = random.nextInt(3) - 1 + squareY;
            while (squareOfMissX == squareX && squareOfMissY == squareY) {
                squareOfMissX = random.nextInt(3) - 1 + squareX;
                squareOfMissY = random.nextInt(3) - 1 + squareY;
            }
            // check if the ball is out
            if ((squareOfMissY < zone.getMinY() && (zone.getName().equals("E") || zone.getName().equals("D"))) ||
                    (squareOfMissX > zone.getMaxX() && zone.getName().equals("D")) ||
                    (squareOfMissY > zone.getMaxY() && (zone.getName().equals("D") || zone.getName().equals("F")))) {
                // print("Player missed and the ball is out (" + squareOfMissX + ", " + squareOfMissY + ")");
                return true;
            } else {
                ball.newLocation(squareOfMissX, squareOfMissY);
                // print("Player missed and the ball is in square (" + ball.getX() + ", " + ball.getY() + ")");
                return false;
            }
        }
    }

    public boolean tacticPitch(int maxX, int maxY, Ball ball, String message, Map<String, Zone> dictOfZones) {
        if (message.equals("Не промах")) {
            ball.newLocation(maxX, maxY);
            // print("Player sent the ball to square (" + ball.getX() + ", " + ball.getY() + ")");
            return false;
        } else {
            Random random = new Random();
            int squareOfMissX = random.nextInt(3) - 1 + maxX;
            int squareOfMissY = random.nextInt(3) - 1 + maxY;
            while (squareOfMissX == maxX && squareOfMissY == maxY) {
                squareOfMissX = random.nextInt(3) - 1 + maxX;
                squareOfMissY = random.nextInt(3) - 1 + maxY;
            }
            // check if the ball is out
            Zone zoneD = dictOfZones.get("D");
            Zone zoneE = dictOfZones.get("E");
            if ((squareOfMissY < zoneE.getMinY()) || (squareOfMissX > zoneD.getMaxX()) || (squareOfMissY > zoneD.getMaxY())) {
                // print("Player missed and the ball is out (" + squareOfMissX + ", " + squareOfMissY + ")");
                return true;
            } else {
                ball.newLocation(squareOfMissX, squareOfMissY);
                // print("Player missed and the ball is in square (" + ball.getX() + ", " + ball.getY() + ")");
                return false;
            }
        }
    }

    public boolean zoneSelection(Map<String, Zone> dictOfZones, Ball ball, String flag, String tactic, Dummy dummy) {
        Random random = new Random();
        if (tactic.equals("random")) {
            String message = "Не промах";
            if (flag.equals("first")) {
                int randZone = random.nextInt(2) + 1;
                Zone zone = dictOfZones.get(randZone == 1 ? "E" : "F");
                pitch(zone, ball, message);
            } else if (flag.equals("default")) {
                int randZone = random.nextInt(3) + 1;
                Zone zone;
                if (randZone == 1) {
                    zone = dictOfZones.get("D");
                } else if (randZone == 2) {
                    zone = dictOfZones.get("E");
                } else {
                    zone = dictOfZones.get("F");
                }
// check if the player missed
                int probabilityOfMiss = random.nextInt(100);
                if (probabilityOfMiss <= 5) {
                    message = "Промах";
                }
                pitch(zone, ball, message);
            } else {
                System.out.println("Invalid type of serve: " + flag);
                throw new IllegalArgumentException();
            }
        } else if (tactic.equals("far square")) {
            String message = "Не промах";
            int dummyX = dummy.getX();
            int dummyY = dummy.getY();
            int maxX = 0;
            int maxY = 0;

            Zone zoneD = dictOfZones.get("D");
            Zone zoneE = dictOfZones.get("E");
            if (flag.equals("first")) {

                if (Math.abs(dummyX - zoneE.getMinX()) >= Math.abs(dummyX - zoneE.getMaxX())) {
                    maxX = zoneE.getMinX();
                } else {
                    maxX = zoneE.getMaxX();
                }

                if (Math.abs(dummyY - zoneD.getMinY()) >= Math.abs(dummyY - zoneD.getMaxY())) {
                    maxY = zoneD.getMinY();
                } else {
                    maxY = zoneD.getMaxY();
                }
                tacticPitch(maxX, maxY, ball, message, dictOfZones);
            } else if (flag.equals("default")) {

                if (Math.abs(dummyX - zoneE.getMinX()) >= Math.abs(dummyX - zoneD.getMaxX())) {
                    maxX = zoneE.getMinX();
                } else {
                    maxX = zoneD.getMaxX();
                }

                if (Math.abs(dummyY - zoneD.getMinY()) >= Math.abs(dummyY - zoneD.getMaxY())) {
                    maxY = zoneD.getMinY();
                } else {
                    maxY = zoneD.getMaxY();
                }

                int probabilityOfMiss = random.nextInt(100);
                if (probabilityOfMiss <= 5) {
                    message = "Промах";
                }
                tacticPitch(maxX, maxY, ball, message, dictOfZones);
            }
        }
        return false;
    }

    public boolean move(Ball ball) {
        int ballX = ball.getX();
        int ballY = ball.getY();
        int numberOfMovements = this.l;
        if (ballX < this.x) {
            while (this.x > ballX) {
                if (numberOfMovements > 0) {
                    this.x -= 1;
                    numberOfMovements -= 1;
                } else {
                    System.out.println("Игрок не смог дотянуться до мяча!");
                    return false;
                }
            }
            while ((this.y + this.r) < ballY || (this.y - this.r) > ballY) {
                if (numberOfMovements > 0) {
                    if ((this.y + this.r) < ballY) {
                        this.y += 1;
                    } else if ((this.y - this.r) > ballY) {
                        this.y -= 1;
                    }
                    numberOfMovements -= 1;
                } else {
                    System.out.println("Игрок не смог дотянуться до мяча!");
                    return false;
                }
            }
        } else if (ballX > this.x) {
            while ((this.y + this.r) < ballY || (this.y - this.r) > ballY) {
                if (numberOfMovements > 0) {
                    if ((this.y + this.r) < ballY) {
                        this.y += 1;
                    } else if ((this.y - this.r) > ballY) {
                        this.y -= 1;
                    }
                    numberOfMovements -= 1;
                } else {
                    System.out.println("Игрок не смог дотянуться до мяча!");
                    return false;
                }
            }
            while ((this.x + this.r) < ballX || (this.x - this.r) > ballX) {
                if (numberOfMovements > 0) {
                    if ((this.x + this.r) < ballX) {
                        this.x += 1;
                    } else if ((this.x - this.r) > ballX) {
                        this.x -= 1;
                    }
                    numberOfMovements -= 1;
                } else {
                    System.out.println("Игрок не смог дотянуться до мяча!");
                    return false;
                }
            }
        }
        System.out.println("Игрок добрался до мяча!");
        return true;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}