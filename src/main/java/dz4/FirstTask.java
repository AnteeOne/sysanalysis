package dz4;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class FirstTask extends AbstractTask {

    @Override
    public void avgAndDispersion(List<Timestamp> dates) {
        Map<Integer, Long> applicationAmountInWhatever = new HashMap<>();
        var firstDay = dates.stream().map(Timestamp::toLocalDateTime)
                .filter(t -> t.getYear() == 2020)
                .filter(t -> t.getMonth() == Month.OCTOBER)
                .filter(localDateTime -> localDateTime.getDayOfMonth() == 1)
                .map(localDateTime -> localDateTime.getHour() + 1)
                .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()));
        var secondDay = dates.stream().map(Timestamp::toLocalDateTime)
                .filter(t -> t.getYear() == 2020)
                .filter(t -> t.getMonth() == Month.OCTOBER)
                .filter(localDateTime -> localDateTime.getDayOfMonth() == 2)
                .map(localDateTime -> localDateTime.getHour() + 25)
                .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()));
        var thirdDay = dates.stream().map(Timestamp::toLocalDateTime)
                .filter(t -> t.getYear() == 2020)
                .filter(t -> t.getMonth() == Month.OCTOBER)
                .filter(localDateTime -> localDateTime.getDayOfMonth() == 3)
                .map(localDateTime -> localDateTime.getHour() + 49)
                .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()));

        applicationAmountInWhatever.putAll(firstDay);
        applicationAmountInWhatever.putAll(secondDay);
        applicationAmountInWhatever.putAll(thirdDay);

        applicationAmountInWhatever
                .forEach((key, value) -> Log.info("Номер часа = {}; Кол-во заявок = {}\n", key, value));

        System.out.println("============ Результат ==============");
        getDispersion(getAverage(applicationAmountInWhatever.values()), applicationAmountInWhatever.values());
    }

    @Override
    protected void getDispersion(double avg, Collection<Long> applicationAmountInHours) {
        var sum = applicationAmountInHours.stream()
                .map(l -> Math.pow((l - avg), 2))
                .reduce(0.0, Double::sum);
        var dispersion = Math.sqrt(sum / (applicationAmountInHours.size()));
        Log.info("Дисперсия = {}\n", dispersion);
    }

    @Override
    protected double getAverage(Collection<Long> applicationAmountInWhatever) {
        var avg = (double) applicationAmountInWhatever.stream()
                .reduce(0L, Long::sum) / applicationAmountInWhatever.size();
        Log.info("Среднее значение = {}\n", avg);
        return avg;
    }
}
