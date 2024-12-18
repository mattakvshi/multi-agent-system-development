package org.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Colony {
    private String name;
    private int level;
    private double balance;
    private double income;
    private double expenses;
    private double experience;
    private List<Effect> effects;
    private boolean alive;
    private boolean isWinner;
    private int roundsPlayed;
    private Integer levelUpIteration;

    public Colony(String name, double balance, double income, double expenses) {
        this.name = name;
        this.level = 1;
        this.balance = balance;
        this.income = income;
        this.expenses = expenses;
        this.experience = 0;
        this.effects = new ArrayList<>();
        this.alive = true;
        this.isWinner = false;
        this.roundsPlayed = 0;
        this.levelUpIteration = null;
    }

    public void checkLevelUp(List<String> log, int cycleNumber) {
        if (!alive) return;

        if (level < Constants.MAX_LEVEL && experience >= Constants.EXPERIENCE_THRESHOLD) {
            experience = 0;
            level++;
            log.add(name + ": Повышение уровня! Новый уровень: " + level + ".");
        }

        if (level == Constants.MAX_LEVEL) {
            isWinner = true;
            levelUpIteration = cycleNumber;
            log.add(name + ": Достигнут максимальный уровень и назначена как победитель.");
            alive = false;
        }
    }

    public void updateBalance(List<String> log) {
        if (!alive) return;

        double previousBalance = balance;
        balance += income - expenses;
        experience += Math.max(0, balance - previousBalance) / 100;
        experience += income / 10;

        if (balance < 0) {
            alive = false;
            log.add(name + ": Баланс отрицателен, колония выбывает!");
        }
    }

    public void applyEffects(List<String> log) {
        Iterator<Effect> iterator = effects.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            effect.apply(this, log);
            effect.durationDecrement();

            if (effect.getDuration() <= 0 && !effect.getName().equals("Максимальный уровень")) {
                effect.rollback(this, log);
                log.add(name + ": Эффект '" + effect.getName() + "' истёк.");
                iterator.remove();
            }
        }
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void setEffects(List<Effect> effects) {
        this.effects = effects;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}


