package dev.marielaroldan.domain.bus;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public record BusLine(int lineNumber,
                      int direction,
                      List<String> busStops) {
    public static final Comparator<BusLine> CMP_BY_STOP_COUNT = Comparator.comparingInt(b -> b.busStops.size());
    public static final Comparator<BusLine> CMP_BY_LINE_NUMBER = Comparator.comparingInt(BusLine::lineNumber);
    public static final Comparator<BusLine> CMP_BY_DIRECTION = Comparator.comparingInt(BusLine::direction);

    @Override
    public String toString() {
        return "Bus Line number = " + lineNumber + "," + "\n" +
                "Bus direction = " + direction + "," + "\n" +
                printStopsNames(busStops);
    }

    private String printStopsNames(List<String> busStops) {
        return busStops.stream()
                .map(p -> "\t" + p)
                .collect(Collectors.joining(" \n", "Bus stops = \n", ""));
    }
}
