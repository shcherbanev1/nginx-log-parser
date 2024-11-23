package backend.academy.service.writer;

import backend.academy.model.LogReport;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@SuppressFBWarnings("VA_FORMAT_STRING_USES_NEWLINE")
@SuppressWarnings("MultipleStringLiterals")
@Slf4j
public class MarkDownWriter extends ReportWriter {

    protected void writeGeneralInfo(Writer writer, LogReport report) throws IOException {
        writer.write("#### Общая информация\n");
        writer.write("| Метрика | Значение |\n");
        writer.write("|:---------------------:|-------------:|\n");
        writer.write(String.format("| Файл(-ы) | %s |\n", report.sources()));
        writer.write(String.format("| Начальная дата | %s |\n", report.from()));
        writer.write(String.format("| Конечная дата | %s |\n", report.to()));
        writer.write(String.format("| Количество запросов | %d |\n", report.totalRequests()));
        writer.write(String.format("| Средний размер ответа | %.2f |\n", report.averageResponseSize()));
        writer.write(String.format("| 95p размера ответа | %.2f |\n", report.responseSize95Percentile()));
    }

    protected void writeFrequentResources(Writer writer, LogReport report) throws IOException {
        writer.write("#### Запрашиваемые ресурсы\n");
        writer.write("| Ресурс | Количество |\n");
        writer.write("|:---------------:|-----------:|\n");
        for (Map.Entry<String, Long> entry : report.mostFrequentResources().entrySet()) {
            writer.write(String.format("| %s | %d |\n", entry.getKey(), entry.getValue()));
        }
    }

    protected void writeResponseCodes(Writer writer, LogReport report) throws IOException {
        writer.write("#### Коды ответа\n");
        writer.write("| Код | Имя | Количество |\n");
        writer.write("|:---:|:---------------------:|-----------:|\n");
        for (Map.Entry<HttpStatus, Long> entry : report.mostFrequentStatusCodes().entrySet()) {
            writer.write(String.format("| %d | %s | %d |\n",
                entry.getKey().value(), entry.getKey().getReasonPhrase(), entry.getValue()));
        }
    }
}
