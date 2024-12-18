package org.example;

import java.util.Random;

class ZombieState extends State {
    private int viewRadius = 15;
    private double viewAngle = Math.toRadians(90);

    public ZombieState(Agent agent) {
        super(agent);
        agent.setSpeed(agent.getBaseSpeed() * 0.9);
    }

    @Override
    public void update() {
        if (new Random().nextDouble() < 0.001) {
            agent.setState(new RecoveredState(agent));
            return;
        }

        Agent target = findVisibleHealthyAgent();
        if (target != null) {
            moveToward(target);
            if (agent.distanceTo(target) < agent.getSimulation().getInfectionRadius()) {
                int incubationPeriod = new Random().nextInt(agent.getSimulation().getTIncMax() - agent.getSimulation().getTIncMin() + 1) + agent.getSimulation().getTIncMin();
                target.setState(new InfectedState(target, incubationPeriod));
            }
        } else {
            agent.moveRandomly();
        }
    }

    private Agent findVisibleHealthyAgent() {
        Agent closestAgent = null;
        double minDistance = Double.MAX_VALUE;

        for (Agent other : agent.getSimulation().getAgents()) {
            if (other.getState() instanceof HealthyState) {
                double distance = agent.distanceTo(other);
                if (distance < viewRadius && inViewAngle(other)) {
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestAgent = other;
                    }
                }
            }
        }
        return closestAgent;
    }

    private boolean inViewAngle(Agent otherAgent) {
        double dx = otherAgent.getX() - agent.getX();
        double dy = otherAgent.getY() - agent.getY();
        double angleToAgent = Math.atan2(dy, dx);
        double relativeAngle = (angleToAgent - agent.getDirection()) % (2 * Math.PI);
        return Math.abs(relativeAngle) < viewAngle / 2;
    }

    private void moveToward(Agent targetAgent) {
        double dx = targetAgent.getX() - agent.getX();
        double dy = targetAgent.getY() - agent.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 0) {
            agent.setX(agent.getX() + (dx / distance) * agent.getSpeed());
            agent.setY(agent.getY() + (dy / distance) * agent.getSpeed());
            agent.setDirection(Math.atan2(dy, dx));
        }
    }
}
