package backend.academy.util;

import backend.academy.type.ReportFormat;

public class ReportUtil {

    public static String generateReportFilePath(String filename, String format) {
        return System.getProperty("user.dir") + "/" + filename + "." + ReportFormat.fromString(format);
    }

}
