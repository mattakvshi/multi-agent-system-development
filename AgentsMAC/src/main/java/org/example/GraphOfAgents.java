package org.example;

import java.util.ArrayList;
import java.util.List;

public class GraphOfAgents {
    private List<Agent> listOfAgents;

    public GraphOfAgents(int n, float p) {
        this.listOfAgents = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            listOfAgents.add(new Agent(i + 1, p));
        }
    }

    public void creatingNeighborhood(Agent agent1, Agent agent2) {
        if (!agent1.getNeighbors().contains(agent2) && !agent2.getNeighbors().contains(agent1)) {
            agent1.addNeighbor(agent2);
            agent2.addNeighbor(agent1);
            System.out.println("Агенты id = " + agent1.getId() + " и id = " + agent2.getId() + " соседи");
        } else {
            System.out.println("Не удалось установить соседство между агентами id = " + agent1.getId() + " и id = " + agent2.getId());
        }
    }

    public boolean checkAgentsBreakdown() {
        int count = 0;
        for (Agent agent : listOfAgents) {
            if (!agent.isExecution()) {
                count++;
            }
        }
        if (count == listOfAgents.size()) {
            System.out.println("Все агенты сломались, дальнейшее выполнение программы невозможно!");
            return false;
        } else {
            return true;
        }
    }

    // Getter for listOfAgents
    public List<Agent> getListOfAgents() {
        return listOfAgents;
    }
}