package dev.marielaroldan.integration.points;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marielaroldan.integration.stops.TestStopPointResponseDeserialization;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TestJourneyPatternPointResponseDeseralization {

    @Test
    void testJsonDeserialization() throws IOException, URISyntaxException {
        var actual = readJourneyPoint();
        var expected = new JourneyPatternPointResponse(new JourneyPatternPointsData(List.of(
                new JourneyPoint("1",
                        "1",
                        "10008",
                        "2022-02-15 00:00:00.000",
                        "2022-02-15 00:00:00.000"),
                new JourneyPoint("1",
                        "1",
                        "10012",
                        "2023-03-07 00:00:00.000",
                        "2023-03-07 00:00:00.000"),
                new JourneyPoint("1",
                        "1",
                        "10014",
                        "2022-08-10 00:00:00.000",
                        "2022-08-10 00:00:00.000")
        )));
        Assertions.assertEquals(expected, actual);
    }

    private JourneyPatternPointResponse readJourneyPoint() throws IOException, URISyntaxException {
        URL resource = TestStopPointResponseDeserialization.class.getClassLoader()
                .getResource("linedata_bus.json");
        return new ObjectMapper()
                .readValue(Files
                        .newBufferedReader(
                                Path.of(resource.toURI())), JourneyPatternPointResponse.class);
    }
}
