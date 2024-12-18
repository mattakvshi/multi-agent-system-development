package org.example;

import java.util.*;

public class Simulation {

    private static List<Artifact> artifactPool = Arrays.asList(
            // Артефакт 15
            new Artifact("Артефакт 15", Arrays.asList(
                    new Effect("experience_current", 10, 1, "Увеличение опыта от текущего опыта"),
                    new Effect("balance", 100, 1, "Увеличение баланса на 100 единиц"),
                    new Effect("expenses_fixed", 50, 3, "Уменьшение текущих расходов на 50 единиц")
            )),

            // Артефакт 40
            new Artifact("Артефакт 40", Arrays.asList(
                    new Effect("income_from_expenses", 10, 1, "Увеличение дохода на 10% от расходов"),
                    new Effect("experience_max", 10, 3, "Увеличение опыта на 10% от максимального опыта"),
                    new Effect("balance", 10, 3, "Увеличение баланса на 10% от дохода")
            )),

            // Артефакт 45
            new Artifact("Артефакт 45", Arrays.asList(
                    new Effect("experience_current", 10, 0, "Увеличение опыта от текущего опыта (единоразово)")
            )),

            // Артефакт 89
            new Artifact("Артефакт 89", Arrays.asList(
                    new Effect("income_double", 0, 1, "Удвоение текущего дохода"),
                    new Effect("expenses_percent", 10, 1, "Уменьшение текущих расходов на 10%"),
                    new Effect("experience_current", 10, 3, "Увеличение опыта от текущего опыта на 10%")
            )),

            // Артефакт 96
            new Artifact("Артефакт 96", Arrays.asList(
                    new Effect("expenses_percent", 10, 1, "Уменьшение расходов на 10% от баланса"),
                    new Effect("level_up", 1, 0, "Увеличение уровня на 1 (единоразово)"),
                    new Effect("income_from_balance", 10, 1, "Увеличение дохода на 10% от баланса")
            ))
    );

    public static void runCycle(List<Colony> activeColonies, List<Colony> winners, List<Colony> losers,
                                int cycleNumber, List<String> log, List<Map<String, Object>> auctionData) {
        log.add("\nЦикл " + cycleNumber + ": Начало.");

        for (Iterator<Colony> iterator = activeColonies.iterator(); iterator.hasNext(); ) {
            Colony colony = iterator.next();
            if (colony.isWinner()) {
                winners.add(colony);
                iterator.remove();
                continue;
            }

            colony.setRoundsPlayed(colony.getRoundsPlayed() + 1);
            colony.applyEffects(log);
            colony.updateBalance(log);
            colony.checkLevelUp(log, cycleNumber);

            if (!colony.isAlive()) {
                if (colony.isWinner()) {
                    winners.add(colony);
                } else {
                    losers.add(colony);
                }
                iterator.remove();
            }
        }

        if (cycleNumber % Constants.EVENT_INTERVAL == 0) {
            log.add("\nСобытие среды:");

            Random random = new Random();
            for (Colony colony : activeColonies) {
                Runnable event = random.nextBoolean()
                        ? () -> Environment.dustStorm(colony, log, 20, 10)
                        : () -> Environment.renaissance(colony, log, 20, 10);
                event.run();
                event.run();
            }
        }

        if (cycleNumber % Constants.AUCTION_INTERVAL == 0) {
            runAuction(activeColonies, log, auctionData);
        }
    }

    public static void runAuction(List<Colony> activeColonies, List<String> log, List<Map<String, Object>> auctionData) {
        log.add("\nАукцион начинается.");
        List<Colony> activeBidders = new ArrayList<>();
        for (Colony colony : activeColonies) {
            if (colony.getBalance() > 50) {
                activeBidders.add(colony);
            }
        }

        if (activeBidders.isEmpty()) {
            log.add("Нет доступных колоний для участия в аукционе.");
            return;
        }

        for (Artifact artifact : artifactPool) {
            if (activeBidders.isEmpty()) {
                break;
            }

            log.add("Лот: " + artifact.getName() + " (" + activeBidders.size() + " участников)");

            // Ставки колоний
            Map<Colony, Integer> bids = new HashMap<>();
            Random random = new Random();
            for (Colony colony : activeBidders) {
                int maxBid = (int) (colony.getBalance() * Constants.MAX_BET);
                int bid = random.nextInt(maxBid - 1) + 1;
                bids.put(colony, bid);
                log.add(colony.getName() + " предложила " + bid + " единиц.");
            }

            // Определение победителя
            Colony winner = Collections.max(bids.entrySet(), Map.Entry.comparingByValue()).getKey();
            int winningBid = bids.get(winner);

            // Сохранение данных о текущем аукционе
            Map<String, Object> auctionRecord = new HashMap<>();
            auctionRecord.put("artifact", artifact.getName());
            auctionRecord.put("winning_bid", winningBid);
            auctionRecord.put("winner_name", winner.getName());
            auctionRecord.put("winner_level", winner.getLevel());
            auctionRecord.put("participants", activeBidders.stream().map(Colony::getLevel).toList());
            auctionData.add(auctionRecord);

            // Применение артефакта и вычет ставки из баланса
            log.add(winner.getName() + " выигрывает лот с ставкой " + winningBid + " единиц!");
            winner.setBalance(winner.getBalance() - winningBid);
            artifact.applyArtifact(winner, log);

            // Удаление победителя из участников аукциона
            activeBidders.remove(winner);
        }

        log.add("Аукцион завершён.");
    }
}