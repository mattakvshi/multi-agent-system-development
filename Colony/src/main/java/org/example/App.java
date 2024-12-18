package org.example;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class App{
    public static void main(String[] args) {
        List<Colony> colonies = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < Constants.COLONY_COUNT; i++) {
            colonies.add(new Colony("Колония " + (i + 1), Constants.INITIAL_BALANCE,
                    random.nextInt(41) + 10, random.nextInt(26) + 5));
        }

        List<String> log = new ArrayList<>();
        List<Colony> activeColonies = new ArrayList<>(colonies);
        List<Colony> winners = new ArrayList<>();
        List<Colony> losers = new ArrayList<>();
        List<Map<String, Object>> auctionData = new ArrayList<>();

        List<Integer> survivalData = new ArrayList<>();
        List<Double> levelProgression = new ArrayList<>();
        List<Integer> levelsGrowth = new ArrayList<>();
        List<List<Double>> balancesByCycle = new ArrayList<>();
        List<Integer> victoryLevels = new ArrayList<>();
        List<Integer> defeatLevels = new ArrayList<>();

        int previousTotalLevel = colonies.stream().mapToInt(Colony::getLevel).sum();
        for (int cycle = 1; cycle <= Constants.SIMULATION_TIME; cycle++) {
            if (activeColonies.isEmpty()) {
                break;
            }
            Simulation.runCycle(activeColonies, winners, losers, cycle, log, auctionData);

            // Обновление данных для графиков
            double avgLevel = activeColonies.stream().mapToInt(Colony::getLevel).average().orElse(0);
            levelProgression.add(avgLevel);

            int currentTotalLevel = activeColonies.stream().mapToInt(Colony::getLevel).sum();
            levelsGrowth.add(currentTotalLevel - previousTotalLevel);
            previousTotalLevel = currentTotalLevel;

            balancesByCycle.add(activeColonies.stream().map(Colony::getBalance).toList());

            for (Colony winner : winners) {
                if (!victoryLevels.contains(winner.getLevel())) {
                    victoryLevels.add(winner.getLevel());
                }
            }

            for (Colony loser : losers) {
                if (!defeatLevels.contains(loser.getLevel())) {
                    defeatLevels.add(loser.getLevel());
                }
            }

            survivalData.add(activeColonies.size());
        }

        // Сохранение лога в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.LOG_FILE))) {
            for (String entry : log) {
                writer.write(entry);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Построение графиков
        Map<Integer, List<Double>> balanceByLevel = new HashMap<>();
        for (Colony colony : colonies) {
            balanceByLevel.computeIfAbsent(colony.getLevel(), k -> new ArrayList<>()).add(colony.getBalance());
        }

        Graphics.plotAverageBalanceByLevel(balanceByLevel); // Средний баланс по уровням
        Graphics.plotAuctionWinningBids(auctionData.stream().map(data -> ((Number) data.get("winning_bid")).doubleValue()).toList()); // Распределение победных ставок

        int survivalCount = activeColonies.size();
        int defeatCount = colonies.size() - survivalCount;
        Graphics.plotSurvivalVsDefeatRatio(survivalCount, defeatCount); // Соотношение выживших и побежденных

        Graphics.plotLevelDistribution(colonies.stream().map(Colony::getLevel).toList()); // Распределение уровней

        Graphics.plotAuctionWinProbabilityByLevel(auctionData); // Вероятность победы в аукционах по уровням

        Graphics.plotLevelGrowthDistribution(levelsGrowth); // Распределение прироста уровня

        Graphics.plotBalanceChange(balancesByCycle); // Изменение балансов колоний

    }
}

