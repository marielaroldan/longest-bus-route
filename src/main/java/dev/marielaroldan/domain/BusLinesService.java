package dev.marielaroldan.domain;

import dev.marielaroldan.domain.bus.BusLine;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Either;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class BusLinesService {

    private final BusAndStopRetriever busAndStopRetriever;

    public BusLinesService(final BusAndStopRetriever busAndStopRetriever) {
        this.busAndStopRetriever = busAndStopRetriever;
    }

    public Either<String, List<BusLine>> getBusLinesWithMostStops(final int n) {

        if (n <= 0) {
            return Either.left("Error: n most be greater than or equals to 1: " + n);
        }
        return getBusLines()
                .map(busLines -> findTopBusLines(busLines, n));
    }

    public Either<String, List<BusLine>> getBusLines() {
        return busAndStopRetriever.getStops()
                .flatMap(
                        validStops -> busAndStopRetriever.getJourneyPoints()
                                .map(validJourney -> mapToBusLineJourneyInfo(validStops, validJourney)));
    }

    private List<BusLine> findTopBusLines(List<BusLine> busLines, int n) {
        var compare = BusLine.CMP_BY_STOP_COUNT.reversed().thenComparing(BusLine.CMP_BY_LINE_NUMBER).thenComparing(BusLine.CMP_BY_DIRECTION);

        return busLines.stream()
                .sorted(compare)
                .limit(n)
                .toList();
    }

    private List<BusLine> mapToBusLineJourneyInfo(final List<StopsPoint> validStops,
                                                  final List<JourneyPoint> validJourney) {

        final var stopById = validStops.stream()
                .collect(toMap(StopsPoint::stopNumber, StopsPoint::stopName));

        return validJourney.stream()
                .map(journeyPoint -> getBusLineTuples(journeyPoint, stopById))
                .collect(groupingBy(t -> t._1, mapping(t -> t._2, toList())))
                .entrySet()
                .stream()
                .map(BusLinesService::mapToBusLine)
                .toList();
    }

    private static BusLine mapToBusLine(Map.Entry<Tuple2<Integer, Integer>, List<String>> busLineEntry) {
        return new BusLine(busLineEntry.getKey()._1, busLineEntry.getKey()._2, busLineEntry.getValue());
    }

    private static Tuple2<Tuple2<Integer, Integer>, String> getBusLineTuples(JourneyPoint journeyPoint,
                                                                             Map<Long, String> stopById) {
        return Tuple
                .of(Tuple.of(journeyPoint.busLineNumber(), journeyPoint.direction()),
                        stopById.getOrDefault(journeyPoint.stopId(),
                                "Unknown"));
    }
}
