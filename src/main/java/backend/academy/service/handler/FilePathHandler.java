package backend.academy.service.handler;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@SuppressFBWarnings("PATH_TRAVERSAL_IN")
@Slf4j
public class FilePathHandler {

    public Stream<Path> getMatchingFiles(String pathPattern) throws IOException {

        Path baseDir = Paths.get(".");
        if (pathPattern.contains("/")) {
            String basePath = pathPattern.substring(0, getIndexForPathRecognizing(pathPattern));
            baseDir = Paths.get(basePath.isEmpty() ? "." : basePath);
        }

        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pathPattern);

        try {
            return Files.walk(baseDir)
                .filter(Files::isRegularFile)
                .filter(matcher::matches);
        } catch (IOException e) {
            log.error("An error occurred while walking the file tree {}", e.getMessage());
            return Stream.empty();
        }
    }

    @SuppressFBWarnings("SLS_SUSPICIOUS_LOOP_SEARCH")
    private int getIndexForPathRecognizing(String path) {
        int index = 0;
        for (int i = 0; i < path.length() && path.charAt(i) != '*'; i++) {
            if (path.charAt(i) == '/') {
                index = i;
            }
        }
        return index;
    }
}
