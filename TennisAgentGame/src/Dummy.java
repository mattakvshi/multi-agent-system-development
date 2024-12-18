import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Dummy {
    private int r;
    private int l;
    private Integer x;  // координаты могут быть null
    private Integer y;
    private int score;
    private int game;
    private int set;
    private int match;

    public Dummy(int r, int l) {
        this.r = r;
        this.l = l;
        this.x = null;
        this.y = null;
        this.score = 0;
        this.game = 0;
        this.set = 0;
        this.match = 0;
    }

    public void pitch(Map<String, Zone> zones, Ball ball) {
        Random rand = new Random();
        int randZone = rand.nextInt(3) + 1;
        Zone zone;
        if (randZone == 1) {
            zone = zones.get("A");
        } else if (randZone == 2) {
            zone = zones.get("B");
        } else {
            zone = zones.get("C");
        }

        int squareX = zone.getMinX() + rand.nextInt(zone.getMaxX() - zone.getMinX() + 1);
        int squareY = zone.getMinY() + rand.nextInt(zone.getMaxY() - zone.getMinY() + 1);
        ball.newLocation(squareX, squareY);
    }

    public boolean move(Ball ball) {
        int ballX = ball.getX();
        int ballY = ball.getY();
        int numberOfMovements = this.l;

        while ((this.y + this.r) < ballY || (this.y - this.r) > ballY) {
            if (numberOfMovements > 0) {
                if ((this.y + this.r) < ballY) {
                    this.y += 1;
                } else if ((this.y - this.r) > ballY) {
                    this.y -= 1;
                }
                numberOfMovements--;
            } else {
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
                numberOfMovements--;
            } else {
                return false;
            }
        }

        return true;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getGame() {
        return game;
    }

    public void setGame(int game) {
        this.game = game;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public int getMatch() {
        return match;
    }

    public void setMatch(int match) {
        this.match = match;
    }

}
