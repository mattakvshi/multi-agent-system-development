package failure.success;

import java.util.HashSet;
import java.util.Set;

public class MessageBus {
    public static boolean exchangePatents(Agent agent1, Agent agent2) {
        agent1.incrementCommunicationRounds();
        agent2.incrementCommunicationRounds();

        Set<String> agent1Needs = new HashSet<>();
        for (String patent : agent1.getTargetPatents()) {
            if (!agent1.getCurrentPatents().contains(patent)) {
                agent1Needs.add(patent);
            }
        }

        Set<String> agent2Needs = new HashSet<>();
        for (String patent : agent2.getTargetPatents()) {
            if (!agent2.getCurrentPatents().contains(patent)) {
                agent2Needs.add(patent);
            }
        }

        boolean exchanged = false;

        for (String patent1 : agent1Needs) {
            if (agent2.getCurrentPatents().contains(patent1)) {
                for (String patent2 : agent2Needs) {
                    if (agent1.getCurrentPatents().contains(patent2)) {
                        agent1.addPatent(patent1);
                        agent2.addPatent(patent2);
                        agent1.removePatent(patent2);
                        agent2.removePatent(patent1);
                        exchanged = true;
                        break;
                    }
                }
            }
        }
        return exchanged;
    }
}
