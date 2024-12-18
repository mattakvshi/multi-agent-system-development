import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Environment {

    private List<Agent> agents; // Список агентов
    private int maxClients; // Максимальное количество клиентов, которые могут быть обслужены

    private double intervalStart; // Начало интервала времени для появления клиентов
    private double intervalEnd; // Конец интервала времени для появления клиентов

    public Environment(int numberOfAgents, int maxClients, double intervalStart, double intervalEnd) {
        this.maxClients = maxClients;
        this.intervalStart = intervalStart;
        this.intervalEnd = intervalEnd;
        agents = new ArrayList<>();
        for (int i = 0; i < numberOfAgents; i++) {
            agents.add(new Agent(i)); // Инициализация агентов с уникальными идентификаторами
        }
    }


    public void startSimulation() {
        int servicedClients = 0; // Количество обслуженных клиентов
        Random random = new Random();
        double currentTime = 0; // Текущее время

        while (servicedClients < maxClients) { // Пока не достигнуто максимальное количество клиентов
            double nextClientTime = intervalStart + (intervalEnd - intervalStart) * random.nextDouble();
            currentTime += nextClientTime; // Обновление текущего времени
            System.out.println("Spawn new client at: " + currentTime);
            Client client = new Client(); // Создание нового клиента

            // Выбор агента с наименьшей загрузкой
            Agent selectedAgent = agents.get(0);
            for (Agent agent : agents) {
                if (agent.getCurrentLoad() < selectedAgent.getCurrentLoad()) {
                    selectedAgent = agent;
                } else if (agent.getCurrentLoad() == selectedAgent.getCurrentLoad() && agent.getId() < selectedAgent.getId()) {
                    selectedAgent = agent;
                }
            }

            selectedAgent.addClient(client); // Добавление клиента в очередь выбранного агента
            servicedClients++; // Увеличение счетчика обслуженных клиентов
        }

        // Обслуживание всех клиентов в очередях агентов после окончания добавления клиентов
        boolean workRemains = true;
        while (workRemains) {
            workRemains = false;
            for (Agent agent : agents) {
                if (!agent.isQueueEmpty()) {
                    agent.serviceClient(); // Обслуживание клиента агентом
                    workRemains = true; // Продолжаем цикл, пока есть необслуженные клиенты
                }
            }
        }

        Collections.sort(agents); // Сортировка агентов для итогового отчета
        printReport(); // Вывод отчета
    }

    private void printReport() {
        for (Agent agent : agents) {
            System.out.println("Agent ID: " + agent.getId() + ", Clients Serviced: " + agent.getTotalServicedClients() + ", Time Spent: " + agent.getTotalTimeSpent());
        }
    }
}