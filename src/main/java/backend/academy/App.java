package backend.academy;

import backend.academy.filter.LogFilter;
import backend.academy.model.LogReport;
import backend.academy.model.LogsStatistic;
import backend.academy.service.LogService;
import backend.academy.service.handler.LogHandler;
import backend.academy.service.writer.ReportWriter;
import backend.academy.util.Args;
import backend.academy.util.InstanceFabric;
import com.beust.jcommander.JCommander;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import static backend.academy.util.InstanceFabric.createFilters;
import static backend.academy.util.InstanceFabric.createLogHandler;
import static backend.academy.util.InstanceFabric.createReportWriter;

@Slf4j
public class App {

    public void run(String[] args) {
        Args arg = new Args();
        if (!"analyzer".equals(args[0])) {
            throw new RuntimeException("Invalid program mode");
        }
        JCommander.newBuilder().addObject(arg).build().parse(Arrays.copyOfRange(args, 1, args.length));

        LogHandler logHandler = createLogHandler(arg.path());
        LogFilter[] filters = createFilters(arg.filterField(), arg.filterValue(), arg.from(), arg.to());
        LogService logService = new LogService();
        LogsStatistic logsStatistic = new LogsStatistic();
        logHandler.fetchLogs(arg.path(), logService, logsStatistic, filters);
        LogReport logReport = logService.generateReport(logsStatistic);
        ReportWriter reportWriter = createReportWriter(arg.format());
        reportWriter.writeReport(logReport, InstanceFabric.generateReportFilePath(arg.reportFilename(), arg.format()));
    }

}
