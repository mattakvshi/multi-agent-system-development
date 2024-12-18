package failure.success;

import java.util.Set;

public class Agent {
    private int id;
    private Set<String> targetPatents;
    private Set<String> currentPatents;
    private int communicationRounds = 0;

    public Agent(int id, Set<String> targetPatents, Set<String> currentPatents) {
        this.id = id;
        this.targetPatents = targetPatents;
        this.currentPatents = currentPatents;
    }

    public int getId() {
        return id;
    }

    public Set<String> getTargetPatents() {
        return targetPatents;
    }

    public Set<String> getCurrentPatents() {
        return currentPatents;
    }

    public int getCommunicationRounds() {
        return communicationRounds;
    }

    public void incrementCommunicationRounds() {
        this.communicationRounds++;
    }

    public boolean hasCompleteSet() {
        return currentPatents.containsAll(targetPatents);
    }

    public boolean needsPatent(String patent) {
        return targetPatents.contains(patent) && !currentPatents.contains(patent);
    }

    public void addPatent(String patent) {
        currentPatents.add(patent);
    }

    public void removePatent(String patent) {
        currentPatents.remove(patent);
    }
}
