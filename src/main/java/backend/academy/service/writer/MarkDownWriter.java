package backend.academy.service.writer;

import backend.academy.model.HttpStatus;
import backend.academy.model.LogReport;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MarkDownWriter extends ReportWriter {

    protected void writeGeneralInfo(Writer writer, LogReport report) throws IOException {
        writer.write("#### Общая информация\n");
        writer.write("| Метрика | Значение |\n" +
            "|:---------------------:|-------------:|\n" +
            "| Файл(-ы) | " + report.sources() + " |\n" +
            "| Начальная дата | " + report.from() + " |\n" +
            "| Конечная дата | " + report.to() + " |\n" +
            "| Количество запросов | " + report.totalRequests() + " |\n" +
            "| Средний размер ответа | " + report.averageResponseSize() + " |\n" +
            "| 95p размера ответа | " + report.responseSize95Percentile() + " |\n");
    }

    protected void writeFrequentResources(Writer writer, LogReport report) throws IOException {
        writer.write("#### Запрашиваемые ресурсы\n");
        writer.write("| Ресурс | Количество |\n" +
            "|:---------------:|-----------:|\n");
        for (Map.Entry<String, Long> entry : report.mostFrequentResources().entrySet()) {
            writer.write("| " + entry.getKey() + " | " + entry.getValue() + " |\n");
        }
    }

    protected void writeResponseCodes(Writer writer, LogReport report) throws IOException {
        writer.write("#### Коды ответа\n");
        writer.write("| Код | Имя | Количество |\n" +
            "|:---:|:---------------------:|-----------:|\n");
        for (Map.Entry<HttpStatus, Long> entry : report.mostFrequentStatusCodes().entrySet()) {
            writer.write(
                "| " + entry.getKey().httpCode() + " | " + entry.getKey().httpStatus() + " | " + entry.getValue() +
                    " |\n");
        }
    }
}
