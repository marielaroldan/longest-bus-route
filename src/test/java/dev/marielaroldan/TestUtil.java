package dev.marielaroldan;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marielaroldan.integration.points.JourneyPatternPointResponse;
import dev.marielaroldan.integration.stops.StopPointResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtil {
    public static JourneyPatternPointResponse readJourneyPoint(String responseFile) throws IOException, URISyntaxException {
        return new ObjectMapper()
                .readValue(getAsStream(responseFile), JourneyPatternPointResponse.class);
    }

    public static String getAsStream(String responseFile) throws IOException, URISyntaxException {
        URL resource = TestUtil.class.getClassLoader()
                .getResource(responseFile);
        return Files
                .readString(Path.of(resource.toURI()));
    }

    public static StopPointResponse readStopPoint(String responseFile) throws IOException, URISyntaxException {
        return new ObjectMapper()
                .readValue(getAsStream(responseFile), StopPointResponse.class);
    }
}
