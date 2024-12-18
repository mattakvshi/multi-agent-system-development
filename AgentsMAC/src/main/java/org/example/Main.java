package org.example;

import java.util.List;


public class  Main {

    public static Tuple<GraphOfAgents, List<Agent>> createGraphOfAgents(int n, double p) {
        GraphOfAgents GraphOfAgents = new GraphOfAgents(n, (float) p);
        List<Agent> list_of_agents = GraphOfAgents.getListOfAgents();
        return new Tuple<>(GraphOfAgents, list_of_agents);
    }

    public static Tuple<GraphOfModules, List<Module>> createGraphOfModules(int m, double a, double b) {
        GraphOfModules GraphOfModules = new GraphOfModules(m, (float) a, (float) b);
        List<Module> list_of_modules = GraphOfModules.getListOfModules();
        return new Tuple<>(GraphOfModules, list_of_modules);
    }

    public static void createModulesDependenciesV1(GraphOfModules GraphOfModules, List<Module> list_of_modules) {
        GraphOfModules.creatingDependencies(list_of_modules.get(0), list_of_modules.get(1));
        GraphOfModules.creatingDependencies(list_of_modules.get(0), list_of_modules.get(2));
        GraphOfModules.creatingDependencies(list_of_modules.get(1), list_of_modules.get(2));
        GraphOfModules.creatingDependencies(list_of_modules.get(1), list_of_modules.get(3));
        GraphOfModules.creatingDependencies(list_of_modules.get(2), list_of_modules.get(3));
        GraphOfModules.creatingDependencies(list_of_modules.get(3), list_of_modules.get(4));
    }

    public static void createModulesDependenciesV2(GraphOfModules GraphOfModules, List<Module> list_of_modules) {
        GraphOfModules.creatingDependencies(list_of_modules.get(0), list_of_modules.get(1));
        GraphOfModules.creatingDependencies(list_of_modules.get(0), list_of_modules.get(2));
        GraphOfModules.creatingDependencies(list_of_modules.get(1), list_of_modules.get(3));
        GraphOfModules.creatingDependencies(list_of_modules.get(2), list_of_modules.get(4));
    }

    public static void createModulesDependenciesV3(GraphOfModules GraphOfModules, List<Module> list_of_modules) {
        GraphOfModules.creatingDependencies(list_of_modules.get(0), list_of_modules.get(3));
        GraphOfModules.creatingDependencies(list_of_modules.get(1), list_of_modules.get(3));
        GraphOfModules.creatingDependencies(list_of_modules.get(2), list_of_modules.get(3));
        GraphOfModules.creatingDependencies(list_of_modules.get(3), list_of_modules.get(4));
        GraphOfModules.creatingDependencies(list_of_modules.get(3), list_of_modules.get(5));
        GraphOfModules.creatingDependencies(list_of_modules.get(3), list_of_modules.get(6));
    }

    public static void createAgentsNeighborhoodV1(GraphOfAgents GraphOfAgents, List<Agent> list_of_agents) {
        GraphOfAgents.creatingNeighborhood(list_of_agents.get(0), list_of_agents.get(1));
        GraphOfAgents.creatingNeighborhood(list_of_agents.get(1), list_of_agents.get(2));
    }

    public static void createAgentsNeighborhoodV2(GraphOfAgents GraphOfAgents, List<Agent> list_of_agents) {
        GraphOfAgents.creatingNeighborhood(list_of_agents.get(0), list_of_agents.get(1));
        GraphOfAgents.creatingNeighborhood(list_of_agents.get(1), list_of_agents.get(2));
        GraphOfAgents.creatingNeighborhood(list_of_agents.get(2), list_of_agents.get(0));
    }

    public static void baseCycle(List<Module> list_of_modules, List<Agent> list_of_agents, GraphOfAgents GraphOfAgents) {
        System.out.println("Начало выполнения программы");

        int i = 0;

        int count = 0;
        for (Module module : list_of_modules) {
            if (!module.isCompleted()) {
                count++;
            }
        }

        while (count > 0) {
            i++;

            for (Agent agent : list_of_agents) {
                for (Module module : list_of_modules) {
                    if (agent.getCurModule() == null) {
                        agent.attemptGetModule(module);
                    }
                }
                if (agent.getCurModule() != null) {
                    agent.checkBreakdown();
                }
            }

            boolean check = GraphOfAgents.checkAgentsBreakdown();
            if (!check) {
                return;
            }

            System.out.println("Текущая итерация = " + i);

            count = 0;
            for (Module module : list_of_modules) {
                if (!module.isCompleted()) {
                    count++;
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Агенты успешно выполнили все модули за " + i + " итераций");
    }

    public static void mainCycle(int m, double a, double b, int n, double p) {
        Tuple<GraphOfModules, List<Module>> modulesInfo = createGraphOfModules(m, a, b);
        createModulesDependenciesV1(modulesInfo.getFirst(), modulesInfo.getSecond());
        Tuple<GraphOfAgents, List<Agent>> agentsInfo = createGraphOfAgents(n, p);
        createAgentsNeighborhoodV1(agentsInfo.getFirst(), agentsInfo.getSecond());

        baseCycle(modulesInfo.getSecond(), agentsInfo.getSecond(), agentsInfo.getFirst());

        modulesInfo = createGraphOfModules(5, 1.0, 7.1);
        createModulesDependenciesV2(modulesInfo.getFirst(), modulesInfo.getSecond());
        agentsInfo = createGraphOfAgents(3, 1.5);
        createAgentsNeighborhoodV2(agentsInfo.getFirst(), agentsInfo.getSecond());

        baseCycle(modulesInfo.getSecond(), agentsInfo.getSecond(), agentsInfo.getFirst());

        modulesInfo = createGraphOfModules(7, 5.0, 10.1);
        createModulesDependenciesV3(modulesInfo.getFirst(), modulesInfo.getSecond());
        agentsInfo = createGraphOfAgents(3, 2.0);
        createAgentsNeighborhoodV2(agentsInfo.getFirst(), agentsInfo.getSecond());

        baseCycle(modulesInfo.getSecond(), agentsInfo.getSecond(), agentsInfo.getFirst());
    }

    public static void main(String[] args) {
        int m = 5;
        double a = 1.0;
        double b = 5.1;
        int n = 3;
        double p = 1.0;

        mainCycle(m, a, b, n, p);
    }
}