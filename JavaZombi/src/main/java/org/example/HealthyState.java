package org.example;

class HealthyState extends State {
    public HealthyState(Agent agent) {
        super(agent);
        agent.setSpeed(agent.getBaseSpeed());
    }

    @Override
    public void update() {
        for (Agent other : agent.getSimulation().getAgents()) {
            if (other.getState() instanceof ZombieState && other.distanceTo(agent) < agent.getSimulation().getInfectionRadius()) {
                agent.setSpeed(agent.getBaseSpeed() * 1.25);
                return;
            }
        }
        agent.setSpeed(agent.getBaseSpeed());
    }
}