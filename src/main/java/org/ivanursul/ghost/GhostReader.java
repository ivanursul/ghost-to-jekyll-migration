package org.ivanursul.ghost;


import javaslang.control.Try;
import lombok.Data;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.ivanursul.ghost.entity.Ghost;

import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.stream.Collectors.joining;

@Data
public class GhostReader {

    private final String ghostLocation;

    public Ghost read() {
        String blog = Try.of(() -> Files.lines(Paths.get(ghostLocation)).collect(joining())).get();
        return Try.of(() -> mapper().readValue(blog, Ghost.class)).get();
    }

    private ObjectMapper mapper() {
        return new ObjectMapper()
                .configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
