package org.example;

class InfectedState extends State {
    private int incubationPeriod;

    public InfectedState(Agent agent, int incubationPeriod) {
        super(agent);
        this.incubationPeriod = incubationPeriod;
        agent.setSpeed(agent.getBaseSpeed() * 0.95);
    }

    @Override
    public void update() {
        incubationPeriod--;
        if (incubationPeriod <= 0) {
            agent.setState(new ZombieState(agent));
        }
    }
}
