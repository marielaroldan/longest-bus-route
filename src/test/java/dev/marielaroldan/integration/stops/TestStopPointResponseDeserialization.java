package dev.marielaroldan.integration.stops;

import dev.marielaroldan.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class TestStopPointResponseDeserialization {
    @Test
    void testJsonDeserialization() throws IOException, URISyntaxException {
        var actual = TestUtil.readStopPoint("linedata_bus_stopsnames.json");
        var expected = new StopPointResponse(new StopPointData(List.of(
                new Stop("10001",
                        "Stadshagsplan",
                        "10001",
                        "59.3373571967995",
                        "18.0214674159693",
                        "A",
                        "BUSTERM",
                        "2022-10-28 00:00:00.000",
                        "2022-10-28 00:00:00.000"),
                new Stop("10002",
                        "John Bergs plan",
                        "10002",
                        "59.3361450073188",
                        "18.0222866342593",
                        "A",
                        "BUSTERM",
                        "2015-09-24 00:00:00.000",
                        "2015-09-24 00:00:00.000"),
                new Stop("10003",
                        "John Bergs plan",
                        "10002",
                        "59.3362549983713",
                        "18.0220232520707",
                        "A",
                        "BUSTERM",
                        "2015-09-24 00:00:00.000",
                        "2015-09-24 00:00:00.000"),
                new Stop("10006",
                        "Arbetargatan",
                        "10006",
                        "59.3352143599364",
                        "18.0270636513120",
                        "A",
                        "BUSTERM",
                        "2022-09-29 00:00:00.000",
                        "2022-09-29 00:00:00.000")
        )), null, "0");
        Assertions.assertEquals(expected, actual);
    }
}
