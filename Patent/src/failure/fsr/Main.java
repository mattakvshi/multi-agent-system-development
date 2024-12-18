package failure.fsr;


import java.util.*;

public class Main {
        public static void main(String[] args) {
            List<Agent> list_of_agents = new ArrayList<>();
            int n = 5;
            for (int i = 0; i < n; i++) {
                Agent agent = new Agent(i + 1);
                agent.targetTask = new ArrayList<>();
                for (int j = 0; j < 5; j++) {
                    agent.targetTask.add((char) (new Random().nextInt(26) + 'a') + "");
                }
                Collections.sort(agent.targetTask);
                list_of_agents.add(agent);
            }
            MassageBus messageBus = new MassageBus();
            messageBus.consilium(list_of_agents);
        }
}
