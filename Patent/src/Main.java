
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {

    private static final int NUMBER_OF_AGENTS = 5;

    private static int timeOfStart = 0;

    private static List<Agent> listOfAgents = new ArrayList<>();

    public static void main(String[] args) {
        createAgent(NUMBER_OF_AGENTS, listOfAgents);
        startEnvironment(listOfAgents, timeOfStart);
    }

    private static void createAgent(int NUMBER_OF_AGENTS, List<Agent> listOfAgents){
        for (int i = 1; i <= NUMBER_OF_AGENTS; i++) {
            Agent agent = new Agent(i);
            agent.getInfo();
            listOfAgents.add(agent);
        }

        List<String> allPatents = new ArrayList<>();
        for (Agent agent : listOfAgents) {
            allPatents.addAll(agent.getTargetTask());
        }

        System.out.println("\nЗаданный набор символов: " + allPatents);

        Collections.shuffle(allPatents, new Random());
        System.out.println("Перемешанный набор: " + allPatents + "\n");


        int patentsPerAgent = allPatents.size() / NUMBER_OF_AGENTS;
        for (int i = 0; i < patentsPerAgent; i++) {
            for (Agent agent : listOfAgents) {
                if (!allPatents.isEmpty()) {
                    String patent = allPatents.remove(0);
                    agent.obtainingAPatent(patent);
                }
            }
        }


        for (Agent agent : listOfAgents) {
            agent.getInfo();
        }
    }

    private static void startEnvironment(List<Agent> listOfAgents, int timeOfStart){
        MessageBus messageBus = new MessageBus();

        while (listOfAgents.stream().filter(agent -> agent.getTargetTask().size() == agent.getImportantPatents().size()).count() != listOfAgents.size()) {

            timeOfStart++;
            System.out.println("\nТекущая итерация: " + timeOfStart);
            messageBus.consilium(listOfAgents);

            // Перебор агентов
            for (Agent agent : listOfAgents) {
                if (agent.getTargetTask().size() != agent.getImportantPatents().size() || agent.getNumberOfIteration() == 0) {
                    agent.updateState();
                }
                agent.getInfo();
            }

            System.out.println("\n");

            try {
                Thread.sleep(500); // Удобство вывода
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n");

        for (Agent agent : listOfAgents) {
            agent.updateState();
            agent.winInfo();
        }

    }

}
