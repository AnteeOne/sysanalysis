package dz4;

import java.sql.Timestamp;
import java.util.List;

public class Main {

    static double lambda = 114 / (14.42 * 14.42);
    static double writeTime = 2.3;
    // частота
    static double frequency = 1 / writeTime;
    // интенсивность нагрузки
    static double ro = lambda / frequency;
    // время ожидания
    static double timeout = 30;

    public static void main(String[] args) {
        var csvReader = new CsvReader();
        var dates = csvReader.readCsv();
        var task1 = new FirstTask();
        System.out.println("=====================================");
        System.out.println("========== Первое задание ===========");
        System.out.println("=====================================");
        task1.avgAndDispersion(dates);
        var task2 = new SecondTask();
        System.out.println("=====================================");
        System.out.println("========== Второе задание ===========");
        System.out.println("=====================================");
        task2.avgAndDispersion(dates);
        var task3 = new ThirdTask();
        System.out.println("=====================================");
        System.out.println("========== Третье задание ===========");
        System.out.println("=====================================");
        task3.avgAndDispersion(dates);
        var task4 = new FourthTask();
        System.out.println("=====================================");
        System.out.println("========== Четвертое задание ========");
        System.out.println("=====================================");
        task4.avgAndDispersion(dates);
        var task5 = new FifthTask();
        System.out.println("=====================================");
        System.out.println("========== Пятое задание ============");
        System.out.println("=====================================");
        task5.avgAndDispersion(dates);
        System.out.println("=====================================");
        System.out.println("========== Шестое задание ===========");
        System.out.println("=====================================");
        System.out.println();
        System.out.println();
        System.out.println("Минимальная дисперсия = 14.42073271547885 (кол. заявок/час) в Task 3: По средам");
        System.out.println("Среднее значение, соответствующее минимальной дисперсии = 114.79166666666667 (кол. заявок/час)");
        System.out.println();
        System.out.println();
        System.out.println();
        tasks(dates);

    }

    public static void tasks(List<Timestamp> timestamps) {

        System.out.println(smoParams(22));

        // Определите значение х, при котором арендовать более 20 ядер не выгодно. (Х при котором невыгодно платить за 21 ядра)

        int cost = 2400;
        int n1 = 20;
        int n2 = 21;
        double p_cancel_20 = smoParams(n1);
        double p_cancel_21 = smoParams(n2);
        double x = cost * (n2 - n1) / (170 * (p_cancel_20 - p_cancel_21));
        System.out.println("x = " + x);
        // X = 1.662257870373066 * E^124
    }


    public static double smoParams(int n) {
        double a = n / ro;
        double b = lambda * timeout;
        double c = Math.exp(b * (1 - a));
        double B = (c - 1) * (1 - a);

        double p0 = 0;
        for (int k = 0; k < n; k++) {
            p0 += (Math.pow(ro, k)) / factorial(k);
        }
        p0 += (Math.pow(ro, n) / factorial(n)) * B;
        p0 = 1 / p0;
        System.out.println("Вероятность того, что все каналы свободны - " + p0);
        double ropown = Math.pow(ro, n);
        double p_cancel = Math.pow(ro, n) / factorial(n) * c * p0;
        System.out.println("Верояность отказа - " + -p_cancel);

        double w0 = timeout * Math.pow(ro, n) / factorial(n) * p0 * (1 + b / 2);
        System.out.println("Среднее время в очереди - " +-w0);

        double Q = 1 - p_cancel;
        System.out.println("Q = " + Q);

        double l_eff = lambda * Q;
        System.out.println("l_eff = " + l_eff);

        double l_cancel = lambda * p_cancel;
        System.out.println("l_cancel = " + -l_cancel);
        System.out.println("-----------------------------");

        return Math.abs(p_cancel);
    }

    public static long factorial(int number) {
        long result = 1;

        for (int factor = 2; factor <= number; factor++) {
            result *= factor;
        }

        return result;
    }
}
