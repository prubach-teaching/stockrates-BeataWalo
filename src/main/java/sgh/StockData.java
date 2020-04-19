package sgh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class StockData {

    public static void getAndProcessChange(String stock) throws IOException {
        String filePath = "data_in/" + stock + ".csv";
        File inFile = new File(filePath);
        if (!inFile.exists()) {
            download("https://query1.finance.yahoo.com/v7/finance/download/" + stock +
                            "?period1=1554504399&period2=1586126799&interval=1d&events=history",
                    filePath);
        }

        Scanner sc = new Scanner(inFile);
        String line = sc.nextLine();

        //creating a new file
        FileWriter outFile = new FileWriter("data_out/" + stock + ".csv");
        outFile.write(line + ",Change(in %)" + "\n");

        //calculating value of change
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] lineElem = line.split(",");
            double open = Double.valueOf(lineElem[1]);
            double close = Double.valueOf(lineElem[4]);
            double change = (close - open) / open;

            //entering next lines with the value of change
            outFile.write(line + "," + change * 100 + "\n");
        }
        outFile.close();
    }

    public static void download(String url, String fileName) throws IOException {
        try (InputStream in = URI.create(url).toURL().openStream()) {
            Files.copy(in, Paths.get(fileName));
        }
    }

    public static void main(String[] args) throws IOException {
        String[] stocks = new String[] { "IBM", "MSFT", "GOOG" };
        for (String s : stocks) {
            getAndProcessChange(s);
        }
    }
}
