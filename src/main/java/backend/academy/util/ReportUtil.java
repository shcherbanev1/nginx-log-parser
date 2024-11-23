package backend.academy.util;

import backend.academy.type.ReportFormat;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReportUtil {

    public static String generateReportFilePath(String filename, String format) {
        return System.getProperty("user.dir") + "/" + filename + "." + ReportFormat.fromString(format).extension();
    }

}
