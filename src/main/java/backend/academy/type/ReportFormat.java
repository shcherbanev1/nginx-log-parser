package backend.academy.type;

import backend.academy.service.writer.AdocWriter;
import backend.academy.service.writer.MarkDownWriter;
import backend.academy.service.writer.ReportWriter;
import lombok.Getter;

@Getter
    public enum ReportFormat {
    ADOC("adoc", new AdocWriter()),
    MARKDOWN("md", new MarkDownWriter());

    private final String extension;
    private final ReportWriter reportWriter;

    ReportFormat(String extension, ReportWriter reportWriter) {
        this.extension = extension;
        this.reportWriter = reportWriter;
    }

    public static ReportFormat fromString(String format) {
        if (format == null || format.isEmpty()) {
            return MARKDOWN;
        }
        for (ReportFormat reportFormat : values()) {
            if (reportFormat.extension.equalsIgnoreCase(format)) {
                return reportFormat;
            }
        }
        return MARKDOWN;
    }
}

