import java.util.Random;

public class Client {
    private static int counter = 0; // Статическая переменная для уникальных идентификаторов клиентов

    private int id; // Уникальный идентификатор клиента
    private int complexity; // Сложность обслуживания клиента

    public Client() {
        this.id = counter++; // Присваивание уникального идентификатора, инкрементирование счетчика
        Random random = new Random();
        this.complexity = 1 + random.nextInt(10); // Генерация случайной сложности от 1 до 10
    }

    public int getId() {
        return id;
    }

    public int getComplexity() {
        return complexity;
    }
}