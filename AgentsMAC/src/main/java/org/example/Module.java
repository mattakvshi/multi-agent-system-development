package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Module {
    private int id;
    private float load;
    private float curLoad;
    private List<Module> dependencies;
    private boolean availability;
    private Agent curAgent;
    private boolean completed;

    public Module(int id, float a, float b) {
        this.id = id;
        this.load = Math.round(new Random().nextFloat() * (b - a) + a * 10.0f) / 10.0f; // Generate random load in range [a, b]
        this.curLoad = this.load;
        this.dependencies = new ArrayList<>();
        this.availability = false;
        this.curAgent = null;
        this.completed = false;
    }

    public boolean checkAvailability() {
        if (!completed) {
            if (curAgent == null) {
                if (dependencies.isEmpty()) {
                    availability = true;
                    System.out.println("Модуль id = " + id + " можно взять на выполнение (стартовый)");
                    return true;
                } else {
                    for (Module dep : dependencies) {
                        if (!dep.isCompleted()) {
                            System.out.println("Модуль id = " + id + " недоступен для выполнения пока не будут выполнены все его зависимости");
                            return false;
                        }
                    }
                    availability = true;
                    System.out.println("Модуль id = " + id + " можно взять на выполнение (все зависимости выполнены)");
                    return true;
                }
            } else {
                System.out.println("Модуль id = " + id + " занят агентом id = " + curAgent.getId());
                return false;
            }
        } else {
            System.out.println("Модуль id = " + id + " уже выполнен");
            return false;
        }
    }

    public void appointmentAgent(Agent agent) {
        if (!completed) {
            if (curAgent == null) {
                if (availability) {
                    curAgent = agent;
                    System.out.println("Модулю id = " + id + " был назначен агент id = " + curAgent.getId());
                } else {
                    System.out.println("Модулю id = " + id + " нельзя назначить агента id = " + agent.getId() + ", т.к. модуль недоступен для выполнения");
                }
            } else {
                System.out.println("Модулю id = " + id + " нельзя назначить агента id = " + agent.getId() + ", т.к. модуль занят агентом id = " + curAgent.getId());
            }
        } else {
            System.out.println("Модулю id = " + id + " нельзя назначить агента id = " + agent.getId() + ", т.к. модуль выполнен");
        }
    }

    public boolean checkExecution() {
        if (!completed) {
            if (curAgent != null) {
                if (availability) {
                    if (curLoad <= 0) {
                        completed = true;
                        System.out.println("Модуль id = " + id + " был выполнен агентом id = " + curAgent.getId());
                        curAgent = null;
                        return true;
                    } else {
                        System.out.println("Модуль id = " + id + " пока не выполнен (текущая нагрузка = " + curLoad + ") агентом id = " + curAgent.getId());
                        return false;
                    }
                } else {
                    System.out.println("Модуль id = " + id + " не нужно проверять, т.к. он недоступен для выполнения");
                    return false;
                }
            } else {
                System.out.println("Модуль id = " + id + " не нужно проверять, т.к. у него нет агента");
                return false;
            }
        } else {
            System.out.println("Модуль id = " + id + " не нужно проверять, т.к. он выполнен");
            return false;
        }
    }

    public boolean loadRecovery() {
        if (!completed) {
            if (curAgent != null) {
                if (availability) {
                    curLoad = load;
                    System.out.println("Модуль id = " + id + " восстановил нагрузку из-за поломки своего агента (id = " + curAgent.getId() + ")");
                    curAgent = null;
                    return true;
                } else {
                    System.out.println("Модулю id = " + id + " не нужно восстанавливать нагрузку, т.к. он недоступен для выполнения");
                    return false;
                }
            } else {
                System.out.println("Модулю id = " + id + " не нужно восстанавливать нагрузку, т.к. у него нет агента (модуль не выполняется)");
                return false;
            }
        } else {
            System.out.println("Модулю id = " + id + " не нужно восстанавливать нагрузку, т.к. он выполнен");
            return false;
        }
    }

    public void addDependency(Module module) {
        this.dependencies.add(module);
    }

    public int getId() {
        return id;
    }

    public List<Module> getDependencies() {
        return dependencies;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isAvailability() {
        return availability;
    }

    public float getCurLoad() {
        return curLoad;
    }

    public void reduceLoad(float amount) {
        this.curLoad -= amount;
    }
}