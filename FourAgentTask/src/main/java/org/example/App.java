package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;


public class App {
    public static void main(String[] args) {
        double t = 0.0;
        double deltaT = 0.01;
        int tStop = 10;
        List<Double> tauList = new ArrayList<>();
        List<Double> kList = new ArrayList<>();
        List<Double> bigT1List = new ArrayList<>();
        List<Double> bigT2List = new ArrayList<>();
        List<Double> bigDList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            double value = i * 10.0 / 3;
            tauList.add(value);
            kList.add(value);
            bigT1List.add(value);
            bigT2List.add(value);
            bigDList.add(value);
        }

        double maxErrorThreshold = 1.0;
        double step = 1.0;

        allGraphics(t, deltaT, tStop, tauList, kList, bigT1List, bigT2List, bigDList, maxErrorThreshold, step);
    }

    public static int calculateG_t(double t, int tStop) {
        double mid = tStop / 2.0;
        return (t >= 0 && t <= mid) ? 0 : 1;
    }

    public static double calculateU_t(double k, int g_t, double x_t_tau) {
        return k * (g_t - x_t_tau);
    }

    public static double calculateInterpolatedX_i(List<Double> allTime, List<Double> allX_i, double tMinusTau) {
        if (allX_i.size() < 2) {
            throw new IllegalArgumentException("Недостаточно данных для интерполяции");
        }

        if (allTime.isEmpty()) {
            throw new IllegalArgumentException("Список allTime не должен быть пустым");
        }

        if (tMinusTau <= allTime.get(0)) {
            return allX_i.get(0);
        }

        int index = -1;
        for (int i = 0; i < allTime.size(); i++) {
            if (allTime.get(i) > tMinusTau) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return allX_i.get(allX_i.size() - 1);
        }
        if (index <= 0) {
            throw new IndexOutOfBoundsException("Индекс интерполяции вне границ: " + index);
        }

        double x1 = allX_i.get(index - 1);
        double x2 = allX_i.get(index);
        double t1 = allTime.get(index - 1);
        double t2 = allTime.get(index);

        return x1 + (x2 - x1) * ((tMinusTau - t1) / (t2 - t1));
    }

    public static double calculateX_i(double bigT1, double bigT2, double bigD, double x_i_prev, double x_i_prev_2, double deltaT, double u_i) {
        return ((2 * bigT2 * x_i_prev) - (bigT2 * x_i_prev_2) + (bigT1 * deltaT * x_i_prev) + (bigD * (deltaT * deltaT) * u_i) + (deltaT * deltaT)) / (bigT2 + (bigT1 * deltaT) + deltaT * deltaT);
    }

    public static double checkStability(int g_t, double x_t) {
        return abs(g_t - x_t);
    }

    public static double simulation(double t, double deltaT, int tStop, double tau, double k, double bigT1, double bigT2, double bigD, double maxErrorThreshold) {
        List<Double> allTime = new ArrayList<>();
        allTime.add(0.0);

        List<Integer> allG_t = new ArrayList<>();
        allG_t.add(0);

        List<Double> allX_i = new ArrayList<>();
        allX_i.add(0.0);
        allX_i.add(0.0);

        List<Double> allStability = new ArrayList<>();
        allStability.add(0.0);

        double currentTime = t;

        while (currentTime < tStop) {
            allTime.add(currentTime);

            int curG_t = calculateG_t(currentTime, tStop);
            allG_t.add(curG_t);

            double tMinusTau = currentTime - tau;
            double curX_t_minus_tau = calculateInterpolatedX_i(allTime, allX_i, tMinusTau);
            double curU_t = calculateU_t(k, curG_t, curX_t_minus_tau);
            double curX_i = calculateX_i(bigT1, bigT2, bigD, allX_i.get(allX_i.size() - 1), allX_i.get(allX_i.size() - 2), deltaT, curU_t);
            allX_i.add(curX_i);

            double curStability = checkStability(curG_t, curX_i);
            allStability.add(curStability);

            if (curStability > maxErrorThreshold) {
                return -1.0;
            }

            currentTime += deltaT;
        }

        return allStability.stream().mapToDouble(v -> v).max().orElse(0.0);
    }

    public static List<List<Double>> refineSimulate(double t, double deltaT, int tStop, List<Double> prevTauList, List<Double> prevKList, List<Double> prevBigT1List,
                                                    List<Double> prevBigT2List, List<Double> prevBigDList, double maxErrorThreshold, double step) {
        List<Double> tauList = new ArrayList<>(prevTauList);
        List<Double> kList = new ArrayList<>(prevKList);
        List<Double> bigT1List = new ArrayList<>(prevBigT1List);
        List<Double> bigT2List = new ArrayList<>(prevBigT2List);
        List<Double> bigDList = new ArrayList<>(prevBigDList);

        List<Double> x1 = new ArrayList<>();
        List<Double> x2 = new ArrayList<>();
        List<Double> y1 = new ArrayList<>();
        List<Double> y2 = new ArrayList<>();
        List<Double> z = new ArrayList<>();
        List<Double> colors = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            System.out.println(i + " попытка сужения диапазонов значений");

            List<List<Double>> tauCritical = new ArrayList<>();
            List<List<Double>> tauStable = new ArrayList<>();
            boolean errorOccurred = false;

            System.out.println("Начало симуляции:");
            for (double tau : tauList) {
                for (double k : kList) {
                    for (double bigT1 : bigT1List) {
                        for (double bigT2 : bigT2List) {
                            for (double bigD : bigDList) {
                                double maxStability = simulation(t, deltaT, tStop, tau, k, bigT1, bigT2, bigD, maxErrorThreshold);
                                if (maxStability == -1.0) {
                                    List<Double> tempList = new ArrayList<>();
                                    tempList.add(tau);
                                    tempList.add(k);
                                    tempList.add(bigT1);
                                    tempList.add(bigT2);
                                    tempList.add(bigD);
                                    tauCritical.add(tempList);
                                    errorOccurred = true;
                                } else {
                                    List<Double> tempList = new ArrayList<>();
                                    tempList.add(tau);
                                    tempList.add(k);
                                    tempList.add(bigT1);
                                    tempList.add(bigT2);
                                    tempList.add(bigD);
                                    tauStable.add(tempList);
                                }
                                x1.add(tau);
                                x2.add(k);
                                y1.add(bigT1);
                                y2.add(bigT2);
                                z.add(bigD);
                                colors.add(maxStability);
                            }
                        }
                    }
                    System.out.println("Симуляция для tau = " + tau + " закончена");
                }
            }

            if (errorOccurred && !tauStable.isEmpty()) {
                double tauMin = tauStable.stream().mapToDouble(l -> l.get(0)).min().orElse(Double.NaN);
                double tauMax = tauStable.stream().mapToDouble(l -> l.get(0)).max().orElse(Double.NaN) - step;
                double kMin = tauStable.stream().mapToDouble(l -> l.get(1)).min().orElse(Double.NaN);
                double kMax = tauStable.stream().mapToDouble(l -> l.get(1)).max().orElse(Double.NaN) - step;
                double bigT1Min = tauStable.stream().mapToDouble(l -> l.get(2)).min().orElse(Double.NaN);
                double bigT1Max = tauStable.stream().mapToDouble(l -> l.get(2)).max().orElse(Double.NaN) - step;
                double bigT2Min = tauStable.stream().mapToDouble(l -> l.get(3)).min().orElse(Double.NaN);
                double bigT2Max = tauStable.stream().mapToDouble(l -> l.get(3)).max().orElse(Double.NaN) - step;
                double bigDMin = tauStable.stream().mapToDouble(l -> l.get(4)).min().orElse(Double.NaN);
                double bigDMax = tauStable.stream().mapToDouble(l -> l.get(4)).max().orElse(Double.NaN) - step;

                tauList = createNewList(tauMin, tauMax, 4);
                kList = createNewList(kMin, kMax, 4);
                bigT1List = createNewList(bigT1Min, bigT1Max, 4);
                bigT2List = createNewList(bigT2Min, bigT2Max, 4);
                bigDList = createNewList(bigDMin, bigDMax, 4);
                System.out.println("Новые диапазоны: tau [" + tauMin + ", " + tauMax + "], k [" + kMin + ", " + kMax + "], T_1 [" + bigT1Min + ", " + bigT1Max + "], T_2 [" + bigT2Min + ", " + bigT2Max + "], D [" + bigDMin + ", " + bigDMax + "]");
            } else if (tauStable.isEmpty()) {
                System.out.println("Все комбинации параметров приводят к ошибке");
                break;
            } else {
                System.out.println("Система устойчива при данных параметрах");
                break;
            }
        }

        List<List<Double>> results = new ArrayList<>();
        results.add(x1);
        results.add(x2);
        results.add(y1);
        results.add(y2);
        results.add(z);
        results.add(colors);
        return results;
    }

    public static List<Double> createNewList(double min, double max, int size) {
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(min + (max - min) * i / (size - 1));
        }
        return list;
    }

    public static JFreeChart createChart(List<Double> x, List<Double> y, String title, String xLabel, String yLabel) {
        XYSeries series = new XYSeries("Симуляция");
        for (int i = 0; i < x.size(); i++) {
            series.add(x.get(i), y.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        return ChartFactory.createXYLineChart(title, xLabel, yLabel, dataset, PlotOrientation.VERTICAL, true, true, false);
    }

    public static void showChart(JFreeChart chart) {
        JFrame frame = new JFrame();
        frame.setContentPane(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void allGraphics(double t, double deltaT, int tStop, List<Double> tauList, List<Double> kList, List<Double> bigT1List, List<Double> bigT2List,
                                   List<Double> bigDList, double maxErrorThreshold, double step) {
        List<List<Double>> results = refineSimulate(t, deltaT, tStop, tauList, kList, bigT1List, bigT2List, bigDList, maxErrorThreshold, step);

        for (String titleSuffix : new String[]{"K", "T1", "T2", "D"}) {
            List<Double> xValues = new ArrayList<>();
            List<Double> yValues = new ArrayList<>();
            for (List<Double> result : results) {
                xValues.add(result.get(0));
            }

            if ("K".equals(titleSuffix)) {
                for (List<Double> result : results) {
                    yValues.add(result.get(1));
                }
            } else if ("T1".equals(titleSuffix)) {
                for (List<Double> result : results) {
                    yValues.add(result.get(2));
                }
            } else if ("T2".equals(titleSuffix)) {
                for (List<Double> result : results) {
                    yValues.add(result.get(3));
                }
            } else if ("D".equals(titleSuffix)) {
                for (List<Double> result : results) {
                    yValues.add(result.get(4));
                }
            }

            JFreeChart chart = createChart(xValues, yValues, "График устойчивости системы tau, " + titleSuffix, "Tau", titleSuffix);
            showChart(chart);
        }
    }
}
