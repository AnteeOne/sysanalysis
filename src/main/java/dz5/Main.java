package dz5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {

    // кол-во дней в подвыборке
    final int portion_size = 5;
    final double A = 3000;
    CurrencyData[] eurrub;
    CurrencyData[] tryrub;
    CurrencyData[] usdrub;
    CurrencyData[] cnyrub;
    // исходная сумма для вложения
    double budget = 1000000;
    // массив их 5 дневных (portion_size) выборок
    Sample[] sampling;

    // пропорции валют в стратегиях
    double[][] strategy = new double[][]{{0.1, 0.15, 0.15, 0.6}, {0.3, 0.3, 0.3, 0.1}, {0.6, 0.1, 0.1, 0.2}, {0.35, 0.35, 0.15, 0.15}};

    double[][] strategy2 = new double[][]{{0.2, 0.15, 0.25, 0.4}, {0.3, 0.25, 0.15, 0.3}, {0.5, 0.15, 0.25, 0.1}, {0.35, 0.25, 0.20, 0.20}};


    // размер всей исследуемой выборки
    int sz;

    public static void main(String[] args) {

        Main prog = new Main();
        // Загружаем данные
        prog.initData();
        // Строим выборку
        prog.sampling();

        prog.makeMatrix();
    }

    static int min(int a1, int a2, int a3, int a4) {
        int x, y;
        return a1 < (y = a2 < (x = a3 < a4 ? a3 : a4) ? a2 : x) ? a1 : y;
    }

    public void sampling() {
        // будем рассматривать выборки по portion_size дней из массива котировок
        // затем по ним фиксировать частоту появления событий при разных стратегиях
        sampling = new Sample[sz / portion_size];

        for (int i = 0; i < sz / portion_size; i++) { // цикл по подвыборкам, их количество = размер массива котировок / portion_size

            System.out.println("Выборка #" + i + " дата с: " + new SimpleDateFormat("dd.MM.yyyy").format(eurrub[i * portion_size].date));

            int l = i * portion_size; // индекс первого элемента подвыборки в общем массиве котировок

            // формируем и обсчитываем выборку из 5 (portion_size) дней
            // в цикле индекс очередной подвыборки формируется как [i/portion_size]
            sampling[i] = new Sample();

            // Расчитываем "прибыль"(profit) - разницу между пересчитанной в рублях валютной корзины по
            // курсам закрытия и исходной суммой для каждой стратегии
            // в i-ый день формируем корзину, в (i + portion_size)-ый день конвертируем в рубли
            for (int k = 0; k < 4; k++) { // цикл по стратегиям
                sampling[i].profit[k] = budget * (
                        strategy[k][0] * ((1 / eurrub[l].open) * eurrub[l + portion_size - 1].close - 1) +
                                strategy[k][1] * ((1 / tryrub[l].open) * tryrub[l + portion_size - 1].close - 1) +
                                strategy[k][2] * ((1 / usdrub[l].open) * usdrub[l + portion_size - 1].close - 1) +
                                strategy[k][3] * ((1 / cnyrub[l].open) * cnyrub[l + portion_size - 1].close - 1)
                );
            }

            // Для подсчета дисперсии по пропорциональной совокупности валют в корзине по каждой стратегии
            // сфомируем "курс" корзины на каждый день подвыборки из portion_size дней (для определенности, по цене закрытия)
            // как сумму произведений курсов валют на соответствующую пропорцию из стратегии
            // По такому "курсу" корзины для разных стратегий вычислим среднее и дисперсию
            double max_dispersion = 0; // переменная для сравнения дисперсий разных стратегий
            String max_dispersion_strat = "";

            for (int k = 0; k < 4; k++) { // цикл по стратегиям
                // среднее
                double x_ = 0;
                for (int j = l; j < l + portion_size; j++) {
                    x_ = x_ + strategy[k][0] * eurrub[j].close + strategy[k][1] * tryrub[j].close +
                            strategy[k][2] * usdrub[j].close + strategy[k][3] * cnyrub[j].close;
                }
                x_ = x_ / portion_size;

                // дисперсия
                double d = 0;
                for (int j = l; j < l + portion_size; j++) {
                    d = d + (strategy[k][0] * eurrub[j].close + strategy[k][1] * tryrub[j].close +
                            strategy[k][2] * usdrub[j].close + strategy[k][3] * cnyrub[j].close - x_) *
                            (strategy[k][0] * eurrub[j].close + strategy[k][1] * tryrub[j].close +
                                    strategy[k][2] * usdrub[j].close + strategy[k][3] * cnyrub[j].close - x_);
                }
                // зафиксируем дисперсию в структуре, хранящей разные параметры подвыборки
                sampling[i].d[k] = d / portion_size;

                // Проверим, не является ли стратегия наиболее рискованной
                if (sampling[i].d[k] > max_dispersion) {
                    max_dispersion = sampling[i].d[k];
                    max_dispersion_strat = "S" + k;
                    sampling[i].risk_strategy = k;
                }


            }

            // Определим состояния "Природы" для стратегий
            for (int k = 0; k < 4; k++) { // цикл по стратегиям
                if (sampling[i].profit[k] > A && k == sampling[i].risk_strategy) {
                    sampling[i].environment_state[k] = 0;
                } else if (sampling[i].profit[k] < A && k == sampling[i].risk_strategy) {
                    sampling[i].environment_state[k] = 1;
                } else if (sampling[i].profit[k] > A && k != sampling[i].risk_strategy) {
                    sampling[i].environment_state[k] = 2;
                } else if (sampling[i].profit[k] < A && k != sampling[i].risk_strategy) {
                    sampling[i].environment_state[k] = 3;
                }
            }

            // распечатаем параметры подвыборки
            for (int k = 0; k < 4; k++) { // цикл по стратегиям
                System.out.println("s" + k + ": profit=" + sampling[i].profit[k] + " , dispersion=" + sampling[i].d[k] + ", environment_state=" + sampling[i].environment_state[k] + "risk " + sampling[i].risk_strategy);


            }

        }
    }

    /*
        Соберем значения в стратегиях с одинаковыми состояниями природы
        например:
        s0 вместе с состоянием  p0 встречается 0 раза
        s0 вместе с состоянием  p1 встречается 0 раза
        s0 вместе с состоянием  p2 встречается 5 раза
        s0 вместе с состоянием  p3 встречается 14 раза
        Тогда средний выигрыш s0 при условии состояния p0 составит 0 руб. с частотой 0
            средний выигрыш s0 при условии состояния p1 составит 0 руб. с частотой 0
            средний выигрыш s0 при условии состояния p2 составит (57419,073814245 / 5)=11483,814762849 руб. с частотой 5/19
            средний выигрыш s0 при условии состояния p3 составит (−60454,60621777 / 14)=−4318,186158412 руб. с частотой 14/19
     */
    public void makeMatrix() {

        System.out.println("----------------------------------------------------------------------------");

        for (int k = 0; k < 4; k++) {// перебираем стратегии
            double p1 = 0;
            double p2 = 0;
            double p3 = 0;
            double p4 = 0; // счетчики выпавших состояний природы при стратегии k
            double x1 = 0;
            double x2 = 0;
            double x3 = 0;
            double x4 = 0; // сумма выигрыша стратегии k при определенном состоянии природы

            for (int i = 0; i < sz / portion_size; i++) { // перебираем выборки и смотрим соответствие стратегии и состояния
                if (sampling[i].environment_state[k] == 0) {
                    p1++;
                    x1 += sampling[i].profit[k];
                } else if (sampling[i].environment_state[k] == 1) {
                    p2++;
                    x2 += sampling[i].profit[k];
                } else if (sampling[i].environment_state[k] == 2) {
                    p3++;
                    x3 += sampling[i].profit[k];
                } else if (sampling[i].environment_state[k] == 3) {
                    p4++;
                    x4 += sampling[i].profit[k];
                }
            }

            if (p1 > 0) {
                x1 = x1 / p1;
                p1 = p1 / (sz / portion_size);
            }
            if (p2 > 0) {
                x2 = x2 / p2;
                p2 = p2 / (sz / portion_size);
            }
            if (p3 > 0) {
                x3 = x3 / p3;
                p3 = p3 / (sz / portion_size);
            }
            if (p4 > 0) {
                x4 = x4 / p4;
                p4 = p4 / (sz / portion_size);
            }

            int num = k + 1;
            // Выводим строку матрицы
//            System.out.println("s" + num + "|" + x1 + "; " + p1 + "|" + x2 + "; " + p2 + "|" + x3 + "; " + p3 + "|" + x4 + "; " + p4 + "|");
//
//            System.out.println(x1 * p1 + x2 * p2 + x3 * p3 + x4 * p4);

            System.out.println("\"x" + num + "1\": \"" + x1 + "\",");
            System.out.println("\"x" + num + "2\": \"" + x2 + "\",");
            System.out.println("\"x" + num + "3\": \"" + x3 + "\",");
            System.out.println("\"x" + num + "4\": \"" + x4 + "\",");
            System.out.println("\"p" + num + "1\": \"" + p1 + "\",");
            System.out.println("\"p" + num + "2\": \"" + p2 + "\",");
            System.out.println("\"p" + num + "3\": \"" + p3 + "\",");
            System.out.println("\"p" + num + "4\": \"" + p4 + "\",");
        }
    }

    public void initData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String pth = "/Users/antee/Documents/Projects/Kotlin/KFU/sysanalysis/src/";


        try {
            List<String> pairEUR_RUB = Files.readAllLines(Paths.get(pth + "EURRUB_221206_221213.txt"));
            List<String> pairTRY_RUB = Files.readAllLines(Paths.get(pth + "TRYRUB_221206_221213.txt"));
            List<String> pairCNY_RUB = Files.readAllLines(Paths.get(pth + "CNYRUB_221206_221213.txt"));
            List<String> pairUSD_RUB = Files.readAllLines(Paths.get(pth + "USDRUB_221206_221213.txt"));

            // Первая строка - заголовок
            sz = min(pairEUR_RUB.size(), pairTRY_RUB.size(), pairUSD_RUB.size(), pairCNY_RUB.size()) - 1;

            eurrub = new CurrencyData[sz];
            tryrub = new CurrencyData[sz];
            usdrub = new CurrencyData[sz];
            cnyrub = new CurrencyData[sz];

            for (int i = 1; i < sz; i++) {
                String[] p_eurrub = pairEUR_RUB.get(i).replace(",", ".").split(";");
                String[] p_jpyrub = pairTRY_RUB.get(i).replace(",", ".").split(";");
                String[] p_usdrub = pairUSD_RUB.get(i).replace(",", ".").split(";");
                String[] p_uzsrub = pairCNY_RUB.get(i).replace(",", ".").split(";");

                eurrub[i - 1] = new CurrencyData(sdf.parse(p_eurrub[2]), Double.parseDouble(p_eurrub[4]), Double.parseDouble(p_eurrub[7]));
                tryrub[i - 1] = new CurrencyData(sdf.parse(p_jpyrub[2]), Double.parseDouble(p_jpyrub[4]), Double.parseDouble(p_jpyrub[7]));
                usdrub[i - 1] = new CurrencyData(sdf.parse(p_usdrub[2]), Double.parseDouble(p_usdrub[4]), Double.parseDouble(p_usdrub[7]));
                cnyrub[i - 1] = new CurrencyData(sdf.parse(p_uzsrub[2]), Double.parseDouble(p_uzsrub[4]), Double.parseDouble(p_uzsrub[7]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

class Sample {
    // выборочная дисперсия по стратегии
    public double[] d = new double[4];
    // "выигрыш" для стратегии
    public double[] profit = new double[4];
    // индекс стратегии с максимальным риском (т.е. максимальной дисперсией)
    public int risk_strategy;
    // Состояния природы для стратегий.
    // Например, environment_state[0] - состояние природы для первой стратегии в этой конкретной выборке
    public int[] environment_state = new int[4];
}

class CurrencyData {

    public Date date;
    public double open;
    public double close;

    public CurrencyData(Date date, double open, double close) {
        this.date = date;
        this.open = open;
        this.close = close;
    }
}