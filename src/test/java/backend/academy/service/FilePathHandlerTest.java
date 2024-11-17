package backend.academy.service;

import backend.academy.service.handler.FilePathHandler;
import java.nio.file.Path;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FilePathHandlerTest {

    private FilePathHandler filePathHandler;

    @BeforeEach
    void setUp() {
        filePathHandler = new FilePathHandler();
    }

    @SneakyThrows
    @Test
    void getFilesByGlobPattern() {
        var actual = filePathHandler.getMatchingFiles("src/test/**/*.log").toList();
        var expected = List.of(Path.of("src/test/resources/logs1.log"), Path.of("src/test/resources/logs2.log"));
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

}
