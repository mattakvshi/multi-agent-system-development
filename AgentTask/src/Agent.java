import java.util.LinkedList;
import java.util.Queue;

public class Agent implements Comparable<Agent> {

    private final int id; // Уникальный идентификатор агента
    private final Queue<Client> clientPool; // Очередь клиентов в дисциплине FIFO
    private int totalServicedClients = 0; // Общее количество обслуженных клиентов
    private int totalTimeSpent = 0; // Общее время, затраченное на обслуживание клиентов
    private int currentLoad = 0; // Текущая загрузка агента (суммарная сложность всех клиентов в очереди)

    public Agent(int id) {
        this.id = id;
        this.clientPool = new LinkedList<>(); // Инициализация очереди клиентов
    }

    public int getId() {
        return id;
    }

    public void addClient(Client client) {
        clientPool.offer(client); // Добавление клиента в очередь
        currentLoad += client.getComplexity(); // Увеличение текущей загрузки агента
    }

    public void serviceClient() {
        if (!clientPool.isEmpty()) { // Если в очереди есть клиенты
            Client client = clientPool.poll(); // Взятие клиента из начала очереди
            totalServicedClients++; // Увеличение счетчика обслуженных клиентов
            totalTimeSpent += client.getComplexity(); // Добавление времени обслуживания клиента
            currentLoad -= client.getComplexity(); // Уменьшение текущей загрузки агента
        }
    }

    public boolean isQueueEmpty() {
        return clientPool.isEmpty(); // Проверка, пуста ли очередь агента
    }

    public int getTotalServicedClients() {
        return totalServicedClients;
    }

    public int getTotalTimeSpent() {
        return totalTimeSpent;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    @Override
    public int compareTo(Agent other) {
        // Сравнение агентов: сначала по количеству обслуженных клиентов, затем по времени обслуживания
        if (this.totalServicedClients != other.totalServicedClients) {
            return other.totalServicedClients - this.totalServicedClients;
        }
        return this.totalTimeSpent - other.totalTimeSpent;
    }
}