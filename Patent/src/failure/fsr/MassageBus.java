package failure.fsr;

import java.util.*;


public class MassageBus {
        public void consilium(List<Agent> list_of_agents) {
            List<Map<String, Integer>> list_of_all_useless_patents = new ArrayList<>();
            boolean successfulTrade = false;
            for (Agent agent : list_of_agents) {
                Map<String, Integer> patentsCount = new HashMap<>();
                for (String patent : agent.uselessPatents) {
                    patentsCount.put(patent, patentsCount.getOrDefault(patent, 0) + 1);
                }
                list_of_all_useless_patents.add(patentsCount);
            }
            for (int i = 0; i < list_of_agents.size(); i++) {
                Agent agent_i = list_of_agents.get(i);
                Map<String, Integer> missingPatents_i = agent_i.checkingTask();
                for (int j = 0; j < list_of_agents.size(); j++) {
                    if (i != j) {
                        Agent agent_j = list_of_agents.get(j);
                        if (anyPatentInList(missingPatents_i, list_of_all_useless_patents.get(j))) {
                            Map<String, Integer> missingPatents_j = agent_j.checkingTask();
                            if (anyPatentInList(missingPatents_j, list_of_all_useless_patents.get(i))) {
                                trade(agent_i, agent_j);
                                list_of_all_useless_patents.set(i, new HashMap<>());
                                for (String patent : agent_i.uselessPatents) {
                                    list_of_all_useless_patents.get(i).put(patent, list_of_all_useless_patents.get(i).getOrDefault(patent, 0) + 1);
                                }
                                list_of_all_useless_patents.set(j, new HashMap<>());
                                for (String patent : agent_j.uselessPatents) {
                                    list_of_all_useless_patents.get(j).put(patent, list_of_all_useless_patents.get(j).getOrDefault(patent, 0) + 1);
                                }
                                successfulTrade = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!successfulTrade) {
                for (int i = 0; i < list_of_agents.size(); i++) {
                    Agent agent = list_of_agents.get(i);
                    if (!agent.checkCompletion()) {
                        System.out.println("Direct trade is not possible for agent " + agent.id + ", searching for a chain of trades");
                        List<Integer> chain = findChain(list_of_agents, i, list_of_all_useless_patents);
                        if (chain != null) {
                            System.out.println("Chain found: " + chain);
                        }
                    }
                }
            }
        }

        private boolean anyPatentInList(Map<String, Integer> map, Map<String, Integer> list) {
            for (String patent : map.keySet()) {
                if (list.containsKey(patent)) {
                    return true;
                }
            }
            return false;
        }

        public void trade(Agent agent_1, Agent agent_2) {
            String patent_1 = null;
            for (String patent : agent_1.uselessPatents) {
                if (agent_2.checkingTask().containsKey(patent)) {
                    agent_1.uselessPatents.remove(patent);
                    agent_2.swap(patent);
                    patent_1 = patent;
                    break;
                }
            }
            String patent_2 = null;
            for (String patent : agent_2.uselessPatents) {
                if (agent_1.checkingTask().containsKey(patent)) {
                    agent_2.uselessPatents.remove(patent);
                    agent_1.swap(patent);
                    patent_2 = patent;
                    break;
                }
            }
            System.out.println("Agents " + agent_1.id + " and " + agent_2.id + " traded patents " + patent_1 + " and " + patent_2);
            agent_1.info();
            agent_2.info();
        }

        public List<Integer> findChain(List<Agent> list_of_agents, int i, List<Map<String, Integer>> list_of_all_useless_patents) {
            System.out.println("Searching for a chain for agent " + list_of_agents.get(i).id);
            Agent current_agent = list_of_agents.get(i);
            List<Integer> path = new ArrayList<>();
            path.add(current_agent.id);
            List<Agent> visited_agents = new ArrayList<>();
            Agent new_current_agent = findNextAgent(list_of_agents, current_agent, path, visited_agents, list_of_all_useless_patents);
            while (path.get(0) != path.get(path.size() - 1)) {
                if (new_current_agent != null) {
                    new_current_agent = findNextAgent(list_of_agents, new_current_agent, path, visited_agents, list_of_all_useless_patents);
                } else {
                    System.out.println("Chain not found for agent " + current_agent.id);
                    return null;
                }
            }
            System.out.println("Chain found: " + path);
            chainExchange(list_of_agents, path, list_of_all_useless_patents);
            return path;
        }

        private Agent findNextAgent(List<Agent> list_of_agents, Agent current_agent, List<Integer> path, List<Agent> visited_agents, List<Map<String, Integer>> list_of_all_useless_patents) {
            for (String useless_patent : current_agent.uselessPatents) {
                for (Agent agent : list_of_agents) {
                    if (agent != current_agent && !visited_agents.contains(agent)) {
                        if (list_of_all_useless_patents.get(list_of_agents.indexOf(agent)).containsKey(useless_patent)) {
                            path.add(agent.id);
                            visited_agents.add(agent);
                            System.out.println("Next agent in chain found: " + agent.id);
                            return agent;
                        }
                    }
                }
            }
            System.out.println("No suitable agent found for chain");
            return null;
        }

        public void chainExchange(List<Agent> list_of_agents, List<Integer> path, List<Map<String, Integer>> list_of_all_useless_patents) {
            for (int i = 0; i < path.size() - 1; i++) {
                Agent agent_i = list_of_agents.get(path.get(i) - 1);
                Agent agent_j = list_of_agents.get(path.get(i + 1) - 1);
                String useless_patent = agent_i.uselessPatents.get(0);
                agent_i.uselessPatents.remove(useless_patent);
                agent_j.swap(useless_patent);
                System.out.println("Agent " + agent_i.id + " gave patent " + useless_patent + " to agent " + agent_j.id);
                agent_i.info();
                agent_j.info();
            }
        }
}
