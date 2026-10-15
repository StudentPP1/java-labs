# Лабораторна робота №2

## Опис проєкту
Аналізатор логів серверів у форматі Common Log Format
(`127.0.0.1 ident alice [01/May/2025:07:20:10 +0000] "GET /index.html HTTP/1.1" 200 9481`).

Клас `ua.kpi.comsys.collections.LogAnalyzer` надає два методи:
- `getUniqueIps(List<String> logLines)` - повертає `Set` усіх унікальних IP-адрес;
- `getTopIps(List<String> logLines, int topN)` - повертає `topN` найчастіших IP-адрес,
  відсортованих за спаданням частоти; за однакової частоти - за алфавітом.

Рядки, що не відповідають Common Log Format (зокрема `null`), ігноруються.
Якщо `logLines == null` або `topN <= 0`, кидається `IllegalArgumentException`.

Проєкт збирається Gradle; залежності (JUnit 5) керуються через Version Catalog
`gradle/libs.versions.toml`. Для колекцій використовується лише стандартний `java.util.*`.

## Визначення варіанта
V = N mod 3 = 15 mod 3 = 0 

## Вимоги
- JDK 21+

## Збірка та запуск тестів
```bash
./gradlew build   
./gradlew test    
```
На Windows: `gradlew.bat build` / `gradlew.bat test`.
