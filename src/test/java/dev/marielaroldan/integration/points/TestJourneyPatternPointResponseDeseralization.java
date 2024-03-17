package dev.marielaroldan.integration.points;

import dev.marielaroldan.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class TestJourneyPatternPointResponseDeseralization {

    @Test
    void testJsonDeserialization() throws IOException, URISyntaxException {
        var actual = TestUtil.readJourneyPoint("linedata_bus.json");
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
        )), null, "0");
        Assertions.assertEquals(expected, actual);
    }


}
