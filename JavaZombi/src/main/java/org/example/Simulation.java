package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Simulation {
    private int n;
    private int m;
    private int tInit;
    private int tIncMin;
    private int tIncMax;
    private int T;
    private double infectionRadius;
    private List<Agent> agents;
    private int time;

    public Simulation(int n, int m, int tInit, int tIncMin, int tIncMax, int T, double infectionRadius) {
        this.n = n;
        this.m = m;
        this.tInit = tInit;
        this.tIncMin = tIncMin;
        this.tIncMax = tIncMax;
        this.T = T;
        this.infectionRadius = infectionRadius;
        this.agents = new ArrayList<>();
        this.time = 0;
        initAgents();
    }

    private void initAgents() {
        for (int i = 0; i < n; i++) {
            double x = new Random().nextDouble() * 1000;
            double y = new Random().nextDouble() * 1000;
            agents.add(new Agent(x, y, HealthyState.class, 5.0, this));
        }
    }

    public void infectInitialAgents() {
        List<Agent> infectedAgents = new ArrayList<>(agents);
        java.util.Collections.shuffle(infectedAgents);
        for (int i = 0; i < m; i++) {
            Agent agent = infectedAgents.get(i);
            int incubationPeriod = new Random().nextInt(tIncMax - tIncMin + 1) + tIncMin;
            agent.setState(new InfectedState(agent, incubationPeriod));
        }
    }

    public void runStep() {
        if (time == tInit) {
            infectInitialAgents();
        }
        for (Agent agent : agents) {
            agent.update();
        }
        time++;
    }

    public int[] countStates() {
        int healthyCount = 0;
        int infectedCount = 0;
        int zombieCount = 0;
        int recoveredCount = 0;

        for (Agent agent : agents) {
            if (agent.getState() instanceof HealthyState) healthyCount++;
            if (agent.getState() instanceof InfectedState) infectedCount++;
            if (agent.getState() instanceof ZombieState) zombieCount++;
            if (agent.getState() instanceof RecoveredState) recoveredCount++;
        }

        return new int[]{healthyCount, infectedCount, zombieCount, recoveredCount};
    }

    public boolean isFinished() {
        int[] counts = countStates();
        int healthyCount = counts[0];
        int infectedCount = counts[1];
        int zombieCount = counts[2];
        int recoveredCount = counts[3];

        if (time >= T) return true;
        if (zombieCount == n || recoveredCount == n) return true;
        if (infectedCount > 0) return false;

        for (Agent agent : agents) {
            if (agent.getState() instanceof InfectedState) {
                return false;
            }
        }

        return zombieCount == n || recoveredCount == n;
    }

    // Getters
    public List<Agent> getAgents() { return agents; }
    public double getInfectionRadius() { return infectionRadius; }
    public int getTIncMin() { return tIncMin; }
    public int getTIncMax() { return tIncMax; }
}

