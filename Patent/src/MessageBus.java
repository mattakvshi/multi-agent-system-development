import java.util.*;
import java.util.stream.Collectors;

public class MessageBus {

    public MessageBus() {}

    public void consilium(List<Agent> listOfAgents) {
        List<Map<String, Long>> listOfAllUselessPatents = new ArrayList<>();
        boolean successfulTrade = false;

        for (Agent agent : listOfAgents) {
            Map<String, Long> patentsCount = agent.getUselessPatents().stream()
                    .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
            listOfAllUselessPatents.add(patentsCount);
        }

        searchLoop:
        for (int i = 0; i < listOfAgents.size(); i++) {
            Map<String, Long> missingPatentsI = listOfAgents.get(i).checkingTask();
            for (int j = 0; j < listOfAgents.size(); j++) {
                if (i != j) {
                    if (missingPatentsI.keySet().stream().anyMatch(listOfAllUselessPatents.get(j)::containsKey)) {
                        Map<String, Long> missingPatentsJ = listOfAgents.get(j).checkingTask();
                        if (missingPatentsJ.keySet().stream().anyMatch(listOfAllUselessPatents.get(i)::containsKey)) {
                            trade(listOfAgents.get(i), listOfAgents.get(j));
                            listOfAllUselessPatents.set(i, listOfAgents.get(i).getUselessPatents().stream()
                                    .collect(Collectors.groupingBy(e -> e, Collectors.counting())));
                            listOfAllUselessPatents.set(j, listOfAgents.get(j).getUselessPatents().stream()
                                    .collect(Collectors.groupingBy(e -> e, Collectors.counting())));
                            successfulTrade = true;
                            break searchLoop;
                        }
                    }
                }
            }
        }

        if (!successfulTrade) {
            for (Agent agent : listOfAgents) {
                if (!agent.checkCompletion()) {
                    System.out.println("Для агента " + agent.getId() + " прямой обмен невозможен, ищем цепочку обменов");
                    List<Integer> chain = findChain(listOfAgents, agent, listOfAllUselessPatents);
                    if (chain != null) {
                        System.out.println("Цепочка нашлась: " + chain);
                    }
                }
            }
        }
    }

    private void trade(Agent agent1, Agent agent2) {
        String patent1 = performSwap(agent1, agent2);
        String patent2 = performSwap(agent2, agent1);

        System.out.println("Агенты " + agent1.getId() + " и " + agent2.getId() + " поменялись патентами " + patent1 + " и " + patent2);
        agent1.getInfo();
        agent2.getInfo();
    }

    private String performSwap(Agent fromAgent, Agent toAgent) {
        String tradedPatent = null;
        Iterator<String> iterator = fromAgent.getUselessPatents().iterator();

        while (iterator.hasNext()) {
            String patent = iterator.next();
            if (toAgent.checkingTask().containsKey(patent)) {
                iterator.remove();
                toAgent.swap(patent);
                tradedPatent = patent;
                break;
            }
        }
        return tradedPatent;
    }

    private List<Integer> findChain(List<Agent> listOfAgents, Agent startAgent, List<Map<String, Long>> listOfAllUselessPatents) {
        System.out.println("Начинается поиск цепи для агента " + startAgent.getId());
        Agent currentAgent = startAgent;
        List<Integer> path = new ArrayList<>(Collections.singletonList(startAgent.getId()));
        Set<Agent> visitedAgents = new HashSet<>();
        Agent newCurrentAgent = findNextAgent(listOfAgents, currentAgent, path, visitedAgents);

        while (!Objects.equals(path.get(0), path.get(path.size() - 1))) {
            if (newCurrentAgent != null) {
                newCurrentAgent = findNextAgent(listOfAgents, newCurrentAgent, path, visitedAgents);
            } else {
                System.out.println("Цепочка для агента " + startAgent + " не сформирована");
                return null;
            }
        }

        System.out.println("Нашлась цепочка агентов: " + path);
        chainExchange(listOfAgents, path, listOfAllUselessPatents);
        return path;
    }

    private Agent findNextAgent(List<Agent> listOfAgents, Agent currentAgent, List<Integer> path, Set<Agent> visitedAgents) {
        for (String uselessPatent : currentAgent.getUselessPatents()) {
            for (Agent agent : listOfAgents) {
                if (!agent.equals(currentAgent) && !visitedAgents.contains(agent)) {
                    if (agent.checkingTask().containsKey(uselessPatent)) {
                        path.add(agent.getId());
                        visitedAgents.add(agent);
                        System.out.println("Следующий агент для цепочки найден: " + agent.getId());
                        return agent;
                    }
                }
            }
        }
        System.out.println("Подходящий агент для цепочки не найден");
        return null;
    }

    private void chainExchange(List<Agent> listOfAgents, List<Integer> path, List<Map<String, Long>> listOfAllUselessPatents) {
        for (int i = 0; i < path.size() - 1; i++) {
            for (Agent agent1 : listOfAgents) {
                if (agent1.getId() == path.get(i)) {
                    String uselessPatent = agent1.getUselessPatents().get(0); // костыль
                    agent1.getUselessPatents().remove(uselessPatent);
                    for (Agent agent2 : listOfAgents) {
                        if (agent2.getId() == path.get(i + 1)) {
                            agent2.swap(uselessPatent);
                            System.out.println("Агент " + agent1.getId() + " отдал патент " + uselessPatent + " агенту " + agent2.getId());
                            agent1.getInfo();
                            agent2.getInfo();
                            listOfAllUselessPatents.set(i, agent1.getUselessPatents().stream()
                                    .collect(Collectors.groupingBy(e -> e, Collectors.counting())));
                        }
                    }
                }
            }
        }
    }

}
