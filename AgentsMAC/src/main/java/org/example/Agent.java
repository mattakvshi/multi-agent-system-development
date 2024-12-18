package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Agent {
    private int id;
    private List<Agent> neighbors;
    private Module curModule;
    private boolean execution;
    private float amountOfExecution;
    private static final float BREAKDOWN_PROBABILITY = 0.05f;

    public Agent(int id, float p) {
        this.id = id;
        this.neighbors = new ArrayList<>();
        this.curModule = null;
        this.execution = true;
        this.amountOfExecution = p;
    }

    public void attemptGetModule(Module module) {
        if (execution) {
            if (curModule == null) {
                if (module.checkAvailability()) {
                    curModule = module;
                    System.out.println("Агент id = " + id + " взял модуль id = " + curModule.getId() + " на выполнение");
                    module.appointmentAgent(this);
                } else {
                    System.out.println("Агент id = " + id + " не может взять модуль id = " + module.getId() + " на выполнение");
                }
            } else {
                System.out.println("Агент id = " + id + " не может взять модуль id = " + module.getId() + ", т.к. у агента уже есть модуль id = " + curModule.getId());
            }
        } else {
            System.out.println("Агент id = " + id + " не может взять модуль id = " + module.getId() + ", т.к. агент сломался");
        }
    }

    public void executionStep() {
        if (execution) {
            if (curModule != null) {
                if (!curModule.checkExecution()) {
                    curModule.reduceLoad(amountOfExecution);
                    System.out.println("Агент id = " + id + " уменьшил нагрузку своего модуля (id = " + curModule.getId() + ") на " + amountOfExecution);
                    if (curModule.checkExecution()) {
                        System.out.println("Агент id = " + id + " выполнил модуль id = " + curModule.getId());
                        curModule = null;
                    }
                } else {
                    System.out.println("Агент id = " + id + " выполнил модуль id = " + curModule.getId());
                    curModule = null;
                }
            } else {
                System.out.println("Агент id = " + id + " не может выполнять модуль, т.к. агент не имеет модуля");
            }
        } else {
            System.out.println("Агент id = " + id + " не может выполнять модуль, т.к. агент сломался");
        }
    }

    public void checkBreakdown() {
        Random rnd = new Random();
        int b = rnd.nextInt(100) + 1; // Random number between 1 and 100

        if (b <= 5) {
            if (execution) {
                if (curModule != null) {
                    Module module = null;
                    if (curModule.loadRecovery()) {
                        execution = false;
                        module = curModule;
                        curModule = null;

                        if (!neighbors.isEmpty()) {
                            for (Agent neighbor : neighbors) {
                                if (neighbor.execution && neighbor.curModule == null) {
                                    neighbor.curModule = module;
                                    System.out.println("Агент id = " + id + " сломался и передал свой модуль id = " + module.getId() + " агенту-соседу id = " + neighbor.id);
                                    module.appointmentAgent(neighbor);
                                    return;
                                } else {
                                    System.out.println("Агент id = " + id + " сломался, и не может передать свой модуль id = " + module.getId() + " агенту-соседу id = " + neighbor.id);
                                }
                            }
                        } else {
                            System.out.println("Агент id = " + id + " не имеет соседей... ОШИБКА");
                            throw new RuntimeException("Agent has no neighbors");
                        }
                    } else {
                        System.out.println("Агент id = " + id + " сломался, а его модуль id = " + module.getId() + " не восстановил нагрузку (0_0)");
                    }
                } else {
                    System.out.println("Агент id = " + id + " не может сломаться без модуля... ОШИБКА");
                    throw new RuntimeException("Agent cannot break down without a module");
                }
            } else {
                System.out.println("Агент id = " + id + " не может сломаться второй раз... ОШИБКА");
                throw new RuntimeException("Agent cannot break down again");
            }
        } else {
            executionStep();
        }
    }

    public void addNeighbor(Agent neighbor) {
        this.neighbors.add(neighbor);
    }

    // Getters
    public int getId() {
        return id;
    }

    public List<Agent> getNeighbors() {
        return neighbors;
    }

    public boolean isExecution() {
        return execution;
    }

    public Module getCurModule() {
        return curModule;
    }
}