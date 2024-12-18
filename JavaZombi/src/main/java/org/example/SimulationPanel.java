package org.example;

import javax.swing.*;
import java.awt.*;

class SimulationPanel extends JPanel {
    private Simulation simulation;

    public SimulationPanel(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Agent agent : simulation.getAgents()) {
            if (agent.getState() instanceof HealthyState) {
                g2d.setColor(new Color(123, 123,0));
            } else if (agent.getState() instanceof InfectedState) {
                g2d.setColor(new Color(79, 100,8));
            } else if (agent.getState() instanceof ZombieState) {
                g2d.setColor( new Color(40, 200,8));
            } else if (agent.getState() instanceof RecoveredState) {
                g2d.setColor(new Color(123, 34,234));
            }

            g2d.fillOval((int) agent.getX(), (int) agent.getY(), 10, 10);
        }
    }
}
