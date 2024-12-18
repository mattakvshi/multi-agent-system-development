package org.example;

import java.util.Random;

class Agent {
    private double x;
    private double y;
    private double baseSpeed;
    private double speed;
    private double direction;
    private State state;
    private Simulation simulation;

    public Agent(double x, double y, Class<? extends State> stateClass, double baseSpeed, Simulation simulation) {
        this.x = x;
        this.y = y;
        this.baseSpeed = baseSpeed;
        this.speed = baseSpeed;
        this.simulation = simulation;
        this.direction = new Random().nextDouble() * 2 * Math.PI;
        try {
            this.state = stateClass.getConstructor(Agent.class).newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        state.update();
        move();
    }

    public void move() {
        if (state instanceof HealthyState || state instanceof InfectedState) {
            moveRandomly();
        }
    }

    public void moveRandomly() {
        direction += (new Random().nextDouble() * 0.2 - 0.1);
        double dx = Math.cos(direction) * speed;
        double dy = Math.sin(direction) * speed;
        x += dx;
        y += dy;
        checkBoundaries();
    }

    public void checkBoundaries() {
        if (x < 0 || x > 1000) {
            direction = Math.PI - direction;
        }
        if (y < 0 || y > 1000) {
            direction = -direction;
        }
        x = Math.max(0, Math.min(1000, x));
        y = Math.max(0, Math.min(1000, y));
    }

    // Getters and setters
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public double getBaseSpeed() { return baseSpeed; }
    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
    public double getDirection() { return direction; }
    public void setDirection(double direction) { this.direction = direction; }
    public State getState() { return state; }
    public void setState(State state) { this.state = state; }
    public Simulation getSimulation() { return simulation; }
    public double distanceTo(Agent other) {
        return Math.sqrt(Math.pow(x - other.getX(), 2) + Math.pow(y - other.getY(), 2));
    }
}