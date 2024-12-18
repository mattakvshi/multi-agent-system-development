package failure.success;

import java.util.*;

public class Environment {

    private List<Agent> agents;

    public Environment(int numberOfAgents, List<String> globalPatents) {
        agents = new ArrayList<>();
        List<String> shuffledPatents = new ArrayList<>(globalPatents);
        Collections.shuffle(shuffledPatents);

        int blockSize = globalPatents.size() / numberOfAgents;
        int extraPatents = globalPatents.size() % numberOfAgents;
        int index = 0;

        for (int i = 0; i < numberOfAgents; i++) {
            int endIndex = index + blockSize + (extraPatents > 0 ? 1 : 0);
            Set<String> targetPatents = new HashSet<>(shuffledPatents.subList(index, endIndex));
            index = endIndex;
            extraPatents--;

            // Вторая порция случайных патентов для текущих патентов, но не совпадающих с целевыми
            Set<String> currentPatents = new HashSet<>();
            while (currentPatents.size() < blockSize) {
                String patent = shuffledPatents.get(new Random().nextInt(shuffledPatents.size()));
                if (!targetPatents.contains(patent)) {
                    currentPatents.add(patent);
                }
            }

            agents.add(new Agent(i, targetPatents, currentPatents));
        }
    }

    public void simulate() {
        boolean allCompleted = false;
        while (!allCompleted) {
            allCompleted = true;
            Collections.shuffle(agents);
            for (int i = 0; i < agents.size() - 1; i++) {
                for (int j = i + 1; j < agents.size(); j++) {
                    Agent agent1 = agents.get(i);
                    Agent agent2 = agents.get(j);
                    if (!agent1.hasCompleteSet() || !agent2.hasCompleteSet()) {
                        allCompleted = false;
                        MessageBus.exchangePatents(agent1, agent2);
                    }
                }
            }
        }
    }

    public void printResults() {
        for (Agent agent : agents) {
            System.out.println("Agent ID: " + agent.getId());
            System.out.println("Target Patents: " + agent.getTargetPatents());
            System.out.println("Current Patents: " + agent.getCurrentPatents());
            System.out.println("Communication Rounds: " + agent.getCommunicationRounds());
            System.out.println();
        }
    }
}
