package backend.academy.service.writer;

import backend.academy.model.HttpStatus;
import backend.academy.model.LogReport;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdocWriter extends ReportWriter {

    protected void writeGeneralInfo(Writer writer, LogReport report) throws IOException {
        writer.write("== Общая информация\n\n");
        writer.write("[options=header]\n");
        writer.write("|===\n");
        writer.write("|Метрика |Значение\n");
        writer.write("|Файл(-ы) |" + report.sources() + "\n");
        writer.write("|Начальная дата |" + report.from() + "\n");
        writer.write("|Конечная дата |" + report.to() + "\n");
        writer.write("|Количество запросов |" + report.totalRequests() + "\n");
        writer.write("|Средний размер ответа |" + report.averageResponseSize() + "\n");
        writer.write("|95p размера ответа |" + report.responseSize95Percentile() + "\n");
        writer.write("|===\n");
    }

    protected void writeFrequentResources(Writer writer, LogReport report) throws IOException {
        writer.write("== Запрашиваемые ресурсы\n\n");
        writer.write("[options=header]\n");
        writer.write("|===\n");
        writer.write("|Ресурс |Количество\n");
        for (Map.Entry<String, Long> entry : report.mostFrequentResources().entrySet()) {
            writer.write("|" + entry.getKey() + " |" + entry.getValue() + "\n");
        }
        writer.write("|===\n");
    }

    protected void writeResponseCodes(Writer writer, LogReport report) throws IOException {
        writer.write("== Коды ответа\n\n");
        writer.write("[options=header]\n");
        writer.write("|===\n");
        writer.write("|Код |Имя |Количество\n");
        for (Map.Entry<HttpStatus, Long> entry : report.mostFrequentStatusCodes().entrySet()) {
            writer.write(
                "|" + entry.getKey().httpCode() + " |" + entry.getKey().httpStatus() + " |" + entry.getValue() + "\n");
        }
        writer.write("|===\n");
    }
}
