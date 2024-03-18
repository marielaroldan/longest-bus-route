package dev.marielaroldan.domain;

import dev.marielaroldan.domain.bus.BusLine;
import io.vavr.control.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BusLinesServiceTest {

    BusAndStopRetriever service;
    BusLinesService busLinesService;

    @BeforeEach
    void init() {
        service = new LocalRetriever("busLine.json", "stopNames.json");
        busLinesService = new BusLinesService(service);
    }

    @Test
    void shouldReturnTheTopStops() {
        service = new LocalRetriever("busLine.json", "stopNames.json");
        busLinesService = new BusLinesService(service);
        List<BusLine> expected = List.of(
                new BusLine(416, 1, List.of("Orminge centrum", "Korset", "Kummelbergets industriområde", "Sarvträsk", "Telegrafberget")),
                new BusLine(416, 2, List.of("Orminge centrum", "Kummelbergets industriområde", "Korset", "Sarvträsk", "Telegrafberget")),
                new BusLine(175, 1, List.of("Akalla", "Stora torget", "Barkarby station", "Barkarby station övre")));
        Either<String, List<BusLine>> actual = busLinesService.getBusLinesWithMostStops(3);
        Assertions.assertEquals(Either.right(expected), actual);
    }

    @Test
    void shouldReturnTheTopStopsWithUnknownSustitution() {
        service = new LocalRetriever("busLine.json",
                "stopNames_for_missing_stops_name_test.json");
        busLinesService = new BusLinesService(service);
        List<BusLine> expected = List.of(
                new BusLine(416, 1, List.of("Unknown", "Korset", "Kummelbergets industriområde", "Sarvträsk", "Telegrafberget")),
                new BusLine(416, 2, List.of("Orminge centrum", "Kummelbergets industriområde", "Korset", "Sarvträsk", "Telegrafberget")),
                new BusLine(175, 1, List.of("Akalla", "Stora torget", "Barkarby station", "Barkarby station övre")));
        Assertions.assertEquals(Either.right(expected),
                busLinesService.getBusLinesWithMostStops(3));
    }

    @Test
    void shouldShowUnknownStopNameWhenThereIsNotInforForStop() {
        service = new LocalRetriever("busLine.json",
                "stopNames_for_missing_stops_name_test.json");
        busLinesService = new BusLinesService(service);

        boolean unknown = busLinesService.getBusLines()
                .get()
                .stream()
                .anyMatch(busLine -> busLine.busStops().contains("Unknown"));
        Assertions.assertTrue(unknown);
    }
}
