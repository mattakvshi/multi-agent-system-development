//package failure;
//
//import java.util.*;
//
//public class Environment {
//
//    private List<Agent> agents;
//    private int communicationRounds = 0;
//
//    public Environment (int numberOfAgents) {
//        List<Patents> helpers = new ArrayList<>();
//        helpers.addAll(Arrays.asList(Patents.values()));
//        Collections.shuffle(helpers, new Random());
//
//        List<String> combinedList = new ArrayList<>();
//        for (Patents patent : helpers) {
//            Collections.addAll(combinedList, patent.getPatient());
//        }
//        Collections.shuffle(combinedList, new Random());
//        int size = combinedList.size() / 5;
//        List<String[]> smallArrays = new ArrayList<>();
//        for (int i = 0; i < numberOfAgents; i++) {
//            String[] smallArray = combinedList.subList(i * size, (i + 1) * size).toArray(new String[0]);
//            smallArrays.add(smallArray);
//        }
//
//        agents = new ArrayList<>();
//        for (int i = 0; i < numberOfAgents; i++) {
//            agents.add(new Agent(i, helpers.get(i).getPatient(), smallArrays.get(i)));
//        }
//
//        startStateCheck(smallArrays);
//    }
//
//
//    public void startingSimulation() {
//        while (!allAgentsCompleted()) {
//            Collections.shuffle(agents, new Random());
//            for (int i = 0; i < agents.size(); i++) {
//                Agent agent1 = agents.get(i);
//                for (int j = 0; j < agents.size(); j++) {
//                    if (i != j) {
//                        Agent agent2 = agents.get(j);
//                        if (!agent1.hasCompleteSet() || !agent2.hasCompleteSet()) {
//                            communicationRounds++;
//                            MessageBus.exchange(agent1, agent2);
//                            System.out.println(agent1.toString() + "----------" + agent2.toString());
//                        }
//                    }
//                }
//            }
//        }
//        printResults();
//    }
//
//    private boolean allAgentsCompleted() {
//        for (Agent agent : agents) {
//            if (!agent.hasCompleteSet()) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public void printResults() {
//        for (Agent agent : agents) {
//            System.out.println("Agent ID: " + agent.getId());
//            System.out.println("Target Patents: " + agent.getTargetPatents().toString());
//            System.out.println("Current Patents: " + agent.getCurrentPatents().toString());
//            System.out.println("Exchange Rounds: " + agent.getExchangeCounter());
//            System.out.println();
//        }
//        System.out.println("Total Communication Rounds: " + communicationRounds);
//    }
//
//
//    public void startStateCheck (List<String []> smallArrays) {
//        for (int i = 0; i < smallArrays.size(); i++) {
//            System.out.println("Start set " + (i + 1) + ": " + String.join(", ", smallArrays.get(i)) + "; Target: " + Arrays.toString(new String[]{agents.get(i).getTargetPatents().toString()}));
//        }
//    }
//}
