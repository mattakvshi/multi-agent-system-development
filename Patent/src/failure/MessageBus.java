package failure;

public class MessageBus {

    public static void exchange(Agent agent1, Agent agent2) {
        agent1.incrementExchangeCounter();
        agent2.incrementExchangeCounter();

        for (String neededPatent1 : agent1.getTargetPatents()) {
            if (agent2.needsPatent(neededPatent1)) {
                for (String neededPatent2 : agent2.getTargetPatents()) {
                    if (agent1.needsPatent(neededPatent2)) {
                        if (agent2.getCurrentPatents().contains(neededPatent1) && agent1.getCurrentPatents().contains(neededPatent2)) {
                            agent1.addPatent(neededPatent1);
                            agent2.addPatent(neededPatent2);
                            agent1.removePatent(neededPatent2);
                            agent2.removePatent(neededPatent1);
                            return;
                        }
                    }
                }
            }
        }

        // Если один агент собрал полный набор, он делится безвозмездно
        if (agent1.hasCompleteSet()) {
            for (String neededPatent2 : agent2.getTargetPatents()) {
                if (agent1.getCurrentPatents().contains(neededPatent2) && !agent2.getCurrentPatents().contains(neededPatent2)) {
                    agent2.addPatent(neededPatent2);
                    return;
                }
            }
        } else if (agent2.hasCompleteSet()) {
            for (String neededPatent1 : agent1.getTargetPatents()) {
                if (agent2.getCurrentPatents().contains(neededPatent1) && !agent1.getCurrentPatents().contains(neededPatent1)) {
                    agent1.addPatent(neededPatent1);
                    return;
                }
            }
        }
    }
}
