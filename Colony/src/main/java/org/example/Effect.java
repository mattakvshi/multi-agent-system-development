package org.example;

import java.util.List;

public class Effect {
    private String effectType;
    private double value;
    private int duration;
    private String name;

    // Конструктор
    public Effect(String effectType, double value, int duration, String name) {
        this.effectType = effectType;
        this.value = value;
        this.duration = duration;
        this.name = name;
    }

    // Применение эффекта
    public void apply(Colony colony, List<String> log) {
        switch (effectType) {
            case "experience_current":
                double experienceIncrease = colony.getExperience() * (value / 100);
                colony.setExperience(colony.getExperience() + experienceIncrease);
                log.add(colony.getName() + ": Опыт увеличен на " + value + "% от текущего опыта.");
                break;
            case "experience_max":
                double maxExperienceIncrease = Constants.EXPERIENCE_THRESHOLD * (value / 100);
                colony.setExperience(colony.getExperience() + maxExperienceIncrease);
                log.add(colony.getName() + ": Опыт увеличен на " + value + "% от максимального опыта уровня.");
                break;
            case "balance":
                colony.setBalance(colony.getBalance() + value);
                log.add(colony.getName() + ": Баланс увеличен на " + value + " единиц.");
                break;
            case "income_from_expenses":
                double incomeIncrease = colony.getExpenses() * (value / 100);
                colony.setIncome(colony.getIncome() + incomeIncrease);
                log.add(colony.getName() + ": Доход увеличен на " + value + "% от расходов.");
                break;
            case "income_double":
                colony.setIncome(colony.getIncome() * 2);
                log.add(colony.getName() + ": Доход удвоен.");
                break;
            case "expenses_fixed":
                colony.setExpenses(colony.getExpenses() - value);
                colony.setExpenses(Math.max(1, colony.getExpenses())); // Убедитесь, что расходы не становятся отрицательными
                log.add(colony.getName() + ": Расходы уменьшены на " + value + " единиц.");
                break;
            case "expenses_percent":
                double expenseReduction = colony.getExpenses() * (value / 100);
                colony.setExpenses(colony.getExpenses() - expenseReduction);
                colony.setExpenses(Math.max(1, colony.getExpenses()));
                log.add(colony.getName() + ": Расходы уменьшены на " + value + "%.");
                break;
            case "level_up":
                int newLevel = (int) (colony.getLevel() + value);
                colony.setLevel(Math.min(Constants.MAX_LEVEL, newLevel));
                log.add(colony.getName() + ": Уровень увеличен на " + (int)value + " уровней.");
                break;
            case "income_from_balance":
                double incomeFromBalance = colony.getBalance() * (value / 100);
                colony.setIncome(colony.getIncome() + incomeFromBalance);
                log.add(colony.getName() + ": Доход увеличен на " + value + "% от баланса.");
                break;
        }
    }

    // Откат эффекта
    public void rollback(Colony colony, List<String> log) {
        // Логика отката эффектов, если применимо
        switch (effectType) {
            case "experience_current":
            case "experience_max":
                // Обычно опыт не откатывается
                break;
            case "balance":
                colony.setBalance(colony.getBalance() - value);
                log.add(colony.getName() + ": Эффект на баланс (" + value + " единиц) снят.");
                break;
            case "income_from_expenses":
                double incomeDecrease = colony.getExpenses() * (value / 100);
                colony.setIncome(colony.getIncome() - incomeDecrease);
                log.add(colony.getName() + ": Эффект на доход (" + value + "% от расходов) снят.");
                break;
            case "income_double":
                colony.setIncome(colony.getIncome() / 2);
                log.add(colony.getName() + ": Эффект удвоения дохода снят.");
                break;
            case "expenses_fixed":
                colony.setExpenses(colony.getExpenses() + value);
                log.add(colony.getName() + ": Эффект на расходы (" + value + " единиц) снят.");
                break;
            case "expenses_percent":
                double expenseIncrease = colony.getExpenses() * (value / 100);
                colony.setExpenses(colony.getExpenses() + expenseIncrease);
                log.add(colony.getName() + ": Эффект на расходы (" + value + "%) снят.");
                break;
            case "level_up":
                // Уровень обычно не уменьшается, поэтому откат может быть не нужен
                break;
            case "income_from_balance":
                double incomeDecreaseFromBalance = colony.getBalance() * (value / 100);
                colony.setIncome(colony.getIncome() - incomeDecreaseFromBalance);
                log.add(colony.getName() + ": Эффект на доход (" + value + "% от баланса) снят.");
                break;
        }
    }

    public void durationDecrement() {
        duration--;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public double getValue() {
        return value;
    }

    public String getEffectType() {
        return effectType;
    }
}