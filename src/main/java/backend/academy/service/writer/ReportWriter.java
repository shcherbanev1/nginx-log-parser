package backend.academy.service.writer;

import backend.academy.model.LogReport;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

@Slf4j
public abstract class ReportWriter {

    public void writeReport(LogReport report, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            writeGeneralInfo(writer, report);
            writer.write("\n");
            writeFrequentResources(writer, report);
            writer.write("\n");
            writeResponseCodes(writer, report);
        } catch (IOException e) {
            log.error("Error while writing report - {}", e.getMessage());
        }
    }

    protected abstract void writeGeneralInfo(Writer writer, LogReport report) throws IOException;

    protected abstract void writeFrequentResources(Writer writer, LogReport report) throws IOException;

    protected abstract void writeResponseCodes(Writer writer, LogReport report) throws IOException;
}
