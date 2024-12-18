public class Zone {
    private String name;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    public Zone(int length, int width, String name) {
        this.name = name;

        switch (name) {
            case "A":
                this.minX = 0;
                this.maxX = (length / 4) - 1;
                this.minY = 0;
                this.maxY = width - 1;
                break;

            case "B":
                this.minX = length / 4;
                this.maxX = (length / 2) - 1;
                this.minY = width / 2;
                this.maxY = width - 1;
                break;

            case "C":
                this.minX = length / 4;
                this.maxX = (length / 2) - 1;
                this.minY = 0;
                this.maxY = (width / 2) - 1;
                break;

            case "D":
                this.minX = (length / 4) * 3;
                this.maxX = length - 1;
                this.minY = 0;
                this.maxY = width - 1;
                break;

            case "E":
                this.minX = length / 2;
                this.maxX = ((length / 4) * 3) - 1;
                this.minY = 0;
                this.maxY = (width / 2) - 1;
                break;

            case "F":
                this.minX = length / 2;
                this.maxX = ((length / 4) * 3) - 1;
                this.minY = width / 2;
                this.maxY = width - 1;
                break;

            default:
                System.out.println("Получено некорректное имя зоны: " + name);
                throw new IllegalArgumentException("Недопустимое имя зоны: " + name);
        }
    }

    public String getName() {
        return name;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }
}