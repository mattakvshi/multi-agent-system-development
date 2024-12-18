package failure;

import java.util.*;

public class Agent {

    private final int id;
    private final Set<String> targetPatents;
    private final Set<String> currentPatents;

    private int exchangeCounter = 0;

    public Agent(int id, String[] targetPatents, String[] currentPatents) {
        this.id = id;
        this.targetPatents = new HashSet<>(Set.of(targetPatents));
        this.currentPatents = new HashSet<>(Set.of(currentPatents));
    }

    public int getExchangeCounter() {
        return exchangeCounter;
    }

    public Set<String> getCurrentPatents() {
        return currentPatents;
    }

    public Set<String> getTargetPatents() {
        return targetPatents;
    }

    public int getId() {
        return id;
    }

    public int getTargetPatentsSize() {
        return targetPatents.size();
    }

    public void incrementExchangeCounter() {
        exchangeCounter++;
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

    @Override
    public String toString() {
        return "Target: " + targetPatents + ", Current Sequence: " + currentPatents;
    }
}
