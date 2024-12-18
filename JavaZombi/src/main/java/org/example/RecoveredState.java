package org.example;

import java.util.Random;

class RecoveredState extends State {
    public RecoveredState(Agent agent) {
        super(agent);
        agent.setSpeed(agent.getBaseSpeed());
    }

    @Override
    public void update() {
        agent.moveRandomly();
        for (Agent other : agent.getSimulation().getAgents()) {
            if (other.getState() instanceof ZombieState && other.distanceTo(agent) < agent.getSimulation().getInfectionRadius()) {
                if (new Random().nextDouble() < 0.64) {
                    agent.setState(new ZombieState(agent));
                    break;
                }
            }
        }
    }
}
