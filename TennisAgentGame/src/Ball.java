
public class Ball {

    private Integer x;  // при инициализации мяча у него нет координат
    private Integer y;

    public Ball() {
        this.x = null;
        this.y = null;
    }

    public void newLocation(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

}
