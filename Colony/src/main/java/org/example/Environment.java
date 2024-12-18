package org.example;

import java.util.List;

public class Environment {

    public static void dustStorm(Colony colony, List<String> log, double incomeReduction, double expenseIncrease) {
        colony.setIncome(colony.getIncome() - colony.getIncome() * (incomeReduction / 100));
        colony.setExpenses(colony.getExpenses() + colony.getExpenses() * (expenseIncrease / 100));
        log.add(colony.getName() + ": Пыльная буря! Доход уменьшен на " + incomeReduction +
                "%, расходы увеличены на " + expenseIncrease + "%.");
    }

    public static void renaissance(Colony colony, List<String> log, double incomeIncrease, double expenseReduction) {
        colony.setIncome(colony.getIncome() + colony.getIncome() * (incomeIncrease / 100));
        colony.setExpenses(Math.max(1, colony.getExpenses() - colony.getExpenses() * (expenseReduction / 100)));
        log.add(colony.getName() + ": Ренессанс! Доход увеличен на " + incomeIncrease +
                "%, расходы уменьшены на " + expenseReduction + "%.");
    }
}

