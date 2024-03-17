package dev.marielaroldan.application;

import dev.marielaroldan.domain.BusLinesService;
import dev.marielaroldan.domain.bus.BusLine;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class BusLineByStops {

    public static void main(String[] args) {

        try {
            final int n = validateInput(args);
            Properties prop = new Properties();
            InputStream resource = BusLineByStops.class.getClassLoader().getResourceAsStream("./config.properties");
            prop.load(resource);
            List<BusLine> topNBusLinesByNStops = getTopNBusLinesByNStops(n, prop.getProperty("url"), prop.getProperty("key"));
            if (!topNBusLinesByNStops.isEmpty()) {
                System.out.println(topNBusLinesByNStops);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int validateInput(String[] args) {
        try {
            if (args.length != 1) {
                System.err.println("Error: Input parameters missing");
                System.exit(1);
            }

            final int n = Integer.parseInt(args[0]);
            if (n <= 0) {
                System.err.println("Error: Input parameter have the incorrect value scope");
                System.exit(1);
            }
            return n;
        } catch (NumberFormatException e) {
            System.err.println("Error: Input parameter have is the incorrect type");
            System.exit(1);
            return -1;
        }
    }

    private static List<BusLine> getTopNBusLinesByNStops(int n, String uri, String key) {
        BusLinesService service = new BusLinesService(uri, key);
        return service.getBusLinesWithMostStops(n)
                .fold(
                        error -> {
                            System.err.println(error);
                            return List.of();
                        },
                        valid -> valid
                );
    }
}
