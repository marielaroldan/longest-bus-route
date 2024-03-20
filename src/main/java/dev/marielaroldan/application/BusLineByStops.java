package dev.marielaroldan.application;

import dev.marielaroldan.domain.BusLinesService;
import dev.marielaroldan.domain.bus.BusLine;
import dev.marielaroldan.integration.TraffickerRetriever;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Either;

import java.util.List;

public class BusLineByStops {

    public static void main(String[] args) {

        var either = validateInput(args)
                .flatMap(input -> getTopNBusLinesByNStops(input._1, input._2));

        if (either.isLeft()) {
            System.err.println(either.getLeft());
            System.exit(1);
        }
        either.get().forEach(System.out::println);
    }

    private static Either<String, Tuple2<Integer, String>> validateInput(String[] args) {
        try {
            if (args.length != 2) {
                return Either.left("Error: Input parameters missing - TopN and API-Key");

            }
            final int n = Integer.parseInt(args[0]);
            if (n <= 0) {
                return Either.left("Error: Number of bus lines to return should be greater than 0: " + args[0]);
            }
            final var apiKey = args[1];
            if (apiKey.isEmpty()) {
                return Either.left("Error: API key cannot be empty");
            }
            return Either.right(Tuple.of(n, apiKey));
        } catch (NumberFormatException e) {
            return Either.left("Error: Number of bus lines to return must be an integer: " + args[0]);
        }
    }

    private static Either<String, List<BusLine>> getTopNBusLinesByNStops(int n, String key) {
        BusLinesService service = new BusLinesService(new TraffickerRetriever(key));
        return service.getBusLinesWithMostStops(n);
    }
}
