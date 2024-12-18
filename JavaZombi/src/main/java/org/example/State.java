package org.example;

abstract class State {
    protected Agent agent;

    public State(Agent agent) {
        this.agent = agent;
    }

    public abstract void update();
}