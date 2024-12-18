package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graphics {

    public static void plotAverageBalanceByLevel(Map<Integer, List<Double>> balanceByLevel) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<Integer, List<Double>> entry : balanceByLevel.entrySet()) {
            int level = entry.getKey();
            double avgBalance = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0);
            dataset.addValue(avgBalance, "Средний баланс" , Integer.valueOf(level));
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Средний баланс по уровням",
                "Уровень",
                "Средний баланс",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));

        JFrame frame = new JFrame();
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void plotAuctionWinningBids(List<Double> winningBids) {
        HistogramDataset dataset = new HistogramDataset();
        double[] bidsArray = winningBids.stream().mapToDouble(Double::doubleValue).toArray();
        dataset.addSeries("Winning Bids", bidsArray, 20);

        JFreeChart histogram = ChartFactory.createHistogram(
                "Распределение победных ставок",
                "Ставка",
                "Количество",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(histogram);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));

        JFrame frame = new JFrame();
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void plotSurvivalVsDefeatRatio(int survivalCount, int defeatCount) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Выжившие", survivalCount);
        dataset.setValue("Побежденные", defeatCount);

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Соотношение выживших и побежденных",
                dataset,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));

        JFrame frame = new JFrame();
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void plotLevelDistribution(List<Integer> levels) {
        HistogramDataset dataset = new HistogramDataset();
        double[] levelsArray = levels.stream().mapToDouble(Integer::doubleValue).toArray();
        dataset.addSeries("Levels", levelsArray, Constants.MAX_LEVEL);

        JFreeChart histogram = ChartFactory.createHistogram(
                "Распределение уровней колоний",
                "Уровень",
                "Количество",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(histogram);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));

        JFrame frame = new JFrame();
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void plotAuctionWinProbabilityByLevel(List<Map<String, Object>> auctionData) {
        Map<Integer, Integer> winsByLevel = new HashMap<>();
        Map<Integer, Integer> participationByLevel = new HashMap<>();

        for (Map<String, Object> data : auctionData) {
            int winnerLevel = (int) data.get("winner_level");
            winsByLevel.put(winnerLevel, winsByLevel.getOrDefault(winnerLevel, 0) + 1);

            List<Integer> participants = (List<Integer>) data.get("participants");
            for (int level : participants) {
                participationByLevel.put(level, participationByLevel.getOrDefault(level, 0) + 1);
            }
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<Integer, Integer> entry : participationByLevel.entrySet()) {
            int level = entry.getKey();
            int participation = entry.getValue();
            if (participation > 0) {
                double probability = winsByLevel.getOrDefault(level, 0) / (double) participation;
                dataset.addValue(probability, "Вероятность победы", String.valueOf(level));
            }
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Вероятность победы в аукционах по уровням",
                "Уровень",
                "Вероятность",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));

        JFrame frame = new JFrame();
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void plotLevelGrowthDistribution(List<Integer> levelsGrowth) {
        HistogramDataset dataset = new HistogramDataset();
        double[] growthArray = levelsGrowth.stream().mapToDouble(Integer::doubleValue).toArray();
        dataset.addSeries("Level Growth", growthArray, 20);

        JFreeChart histogram = ChartFactory.createHistogram(
                "Распределение прироста уровня",
                "Прирост уровня",
                "Количество",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(histogram);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));

        JFrame frame = new JFrame();
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }


    public static void plotBalanceChange(List<List<Double>> balancesByCycle) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < balancesByCycle.size(); i++) {
            List<Double> balances = balancesByCycle.get(i);
            for (int j = 0; j < balances.size(); j++) {
                dataset.addValue(balances.get(j), "Цикл " + (i + 1), String.valueOf(j + 1));
            }
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Изменение балансов колоний",
                "Цикл",
                "Баланс",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));

        JFrame frame = new JFrame();
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

}

