package failure.fsr;

import java.util.*;

public class Agent {
    int id;
    List<String> targetTask;
    private List<String> importantPatents;
    List<String> uselessPatents;
    private int numberOfIteration;
    private int numberOfCommunications;

    public Agent(int id) {
        this.id = id;
        this.targetTask = new ArrayList<>();
        this.importantPatents = new ArrayList<>();
        this.uselessPatents = new ArrayList<>();
        this.numberOfIteration = 0;
        this.numberOfCommunications = 0;
    }

    public void info() {
        Map<String, Integer> missingPatents = checkingTask();
        List<String> misImpPatList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : missingPatents.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                misImpPatList.add(entry.getKey());
            }
        }
        Collections.sort(misImpPatList);
        System.out.println("Agent: id = " + id + "; target task: " + targetTask + "; collected patents: " + importantPatents + "; useless patents: " + uselessPatents + ", needed patents: " + misImpPatList);
    }

    public void obtainingAPatent(String patent) {
        int countPatent = Collections.frequency(targetTask, patent);
        if (countPatent > 0 && Collections.frequency(importantPatents, patent) < countPatent) {
            importantPatents.add(patent);
            Collections.sort(importantPatents);
        } else {
            uselessPatents.add(patent);
            Collections.sort(uselessPatents);
        }
    }

    public void swap(String patent) {
        numberOfCommunications++;
        obtainingAPatent(patent);
    }

    public void updateState() {
        numberOfIteration++;
    }

    public Map<String, Integer> checkingTask() {
        Map<String, Integer> targetPatentCount = new HashMap<>();
        for (String patent : targetTask) {
            targetPatentCount.put(patent, targetPatentCount.getOrDefault(patent, 0) + 1);
        }
        Map<String, Integer> collectedPatentCount = new HashMap<>();
        for (String patent : importantPatents) {
            collectedPatentCount.put(patent, collectedPatentCount.getOrDefault(patent, 0) + 1);
        }
        Map<String, Integer> missingPatents = new HashMap<>();
        for (Map.Entry<String, Integer> entry : targetPatentCount.entrySet()) {
            int collectedCount = collectedPatentCount.getOrDefault(entry.getKey(), 0);
            if (collectedCount < entry.getValue()) {
                missingPatents.put(entry.getKey(), entry.getValue() - collectedCount);
            }
        }
        return missingPatents;
    }

    public boolean checkCompletion() {
        Map<String, Integer> targetPatentCount = new HashMap<>();
        for (String patent : targetTask) {
            targetPatentCount.put(patent, targetPatentCount.getOrDefault(patent, 0) + 1);
        }
        Map<String, Integer> collectedPatentCount = new HashMap<>();
        for (String patent : importantPatents) {
            collectedPatentCount.put(patent, collectedPatentCount.getOrDefault(patent, 0) + 1);
        }
        return targetPatentCount.equals(collectedPatentCount);
    }

    public void winInfo() {
        System.out.println("Agent " + id + " needed " + numberOfIteration + " iterations and " + numberOfCommunications + " communications to collect the target set " + targetTask);
    }
}

