

public class Main {

    public static void main(String[] args) {

        int numberOfAgents = 10; // Количество агентов
        int maxClients = 100; // Максимальное количество клиентов
        double intervalStart = 2.0; // Начало интервала времени для появления клиентов
        double intervalEnd = 5.0; // Конец интервала времени для появления клиентов

        Environment environment = new Environment(numberOfAgents, maxClients, intervalStart, intervalEnd);
        environment.startSimulation(); // Запуск симуляции
    }

}