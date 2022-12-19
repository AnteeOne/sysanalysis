package dz5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PairsConverter {

    public static void main(String[] args) {
        PairsConverter pairsConverter = new PairsConverter();

        String pth = "/Users/antee/Documents/Projects/Kotlin/KFU/sysanalysis/src/";

        File file = pairsConverter.convertCurrency(
                pth + "USDTRY_221206_221213.txt",
                pth + "USDRUB_221206_221213.txt");
    }

    public File convertCurrency(String from, String to) {
        try {
            List<String> resultPair = new ArrayList<>();
            List<String> pairForm = Files.readAllLines(Paths.get(from));
            List<String> pairTo = Files.readAllLines(Paths.get(to));

            if (pairForm.size() != pairTo.size()) {
                throw new IllegalArgumentException("Size is different");
            }

            resultPair.add("<TICKER>;<PER>;<DATE>;<TIME>;<OPEN>;<HIGH>;<LOW>;<CLOSE>;<VOL>");

            String tickerFrom = pairForm.get(1).split(";")[0];
            String tickerTo = pairTo.get(1).split(";")[0];

            String tickerResult = tickerFrom.substring(3) + tickerTo.substring(3);

            String resultFileName = tickerResult + to.substring(6);

            for (int i = 1; i < pairForm.size(); i++) {
                String[] pFrom = pairForm.get(i).split(";");
                String[] pTo = pairTo.get(i).split(";");

                double open_ar = Double.parseDouble(pTo[4]) / Double.parseDouble(pFrom[4]);
                double close_ar = Double.parseDouble(pTo[7]) / Double.parseDouble(pFrom[7]);
                resultPair.add(
                        tickerResult + ";D;" + pTo[2] + ";0;" + String.format("%.7f", open_ar)
                                .replaceAll(",", ".") + ";" +
                                ";" +
                                ";" +
                                String.format("%.7f", close_ar).replaceAll(",", ".") + ";");

                Files.write(Paths.get(resultFileName), resultPair);
            }
            return new File(resultFileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}
