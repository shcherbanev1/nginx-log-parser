package backend.academy.util;

import com.beust.jcommander.Parameter;
import java.util.List;
import lombok.Getter;

@Getter
public class Args {

    @Parameter(names = {"--path", "-p"}, description = "Path to file with logs")
    private String path;

    @Parameter(names = {"--filter-field"})
    private List<String> filterField;

    @Parameter(names = {"--filter-value"})
    private List<String> filterValue;

    @Parameter(names = {"--format"})
    private String format;

    @Parameter(names = {"--from"})
    private String from;

    @Parameter(names = {"--to"})
    private String to;

    @Parameter(names = {"--reportName"})
    private String reportFilename;

}
