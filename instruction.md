### Как запустить?

Перед запуском необходимо сбилдить в jar:

``` shell
mvn clean package
```

Для запуска:

```shell
java -jar target/java-1.0.0-jar-with-dependencies.jar analyzer --path {http resourse/glob pattern} --reportName {reportName}
```

### Есть поддержка методов фильтации:

| available filter |                                                  description |
|:----------------:|-------------------------------------------------------------:|
|       from       | в отчет попадут логи начиная с этой даты - формат yyyy-mm-dd |
|        to        |        в отчет попадут логи до этой даты - формат yyyy-mm-dd |
|      method      |              в отчет попадут логи только с выбранным методом |
|     httpCode     |           в отчет попадут логи ответ на которые был httpCode |
|      agent       |                       в отчет попадут логи с указанным agent |
|  remote-address  |              в отчет попадут логи с указанным remote-address |
|   remote-user    |                 в отчет попадут логи с указанным remote-user | 
|      bytes       |                       в отчет попадут логи с указанным bytes | 
|     request      |                     в отчет попадут логи с указанным request | 
При применении нескольких фильтров указывать следующим образом:
```shell
--filter-field method,bytes --filter-value GET,100
```

### Поддержка 2ух форматов отчета - adoc, markdown
`
--format {format}
`
### Можно указать имя отчета
`
--reportName {reportName}
По умолчанию - report
`
### Пример
получение логов
с https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs у которых
метод запроса = GET


```shell
java -jar target/java-1.0.0-jar-with-dependencies.jar analyzer --path https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs --filter-field method --filter-value GET
```

