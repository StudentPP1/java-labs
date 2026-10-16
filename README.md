# Технології розробки на платформі Java - лабораторні роботи

Варіант: V = N mod 3 = 15 mod 3 = **0**

| Лабораторна | Тема | Збірка |
|---|---|---|
| [lab1](lab1/) | Сучасні можливості Java та Data-Oriented Programming (система управління замовленнями) | Maven, JDK 25 |
| [lab2](lab2/) | Gradle та Java Collections Framework (аналізатор логів серверів) | Gradle, JDK 21+ |
| [lab3](lab3/) | Stream API та кастомний Gatherer (аналіз потоку транзакцій) | Gradle, JDK 25 |

Детальний опис кожної роботи - у README відповідної папки.

## Запуск тестів

```bash
# lab1
cd lab1
mvn test

# lab2
cd lab2
./gradlew test      # Windows: gradlew.bat test

# lab3
cd lab3
./gradlew test      # Windows: gradlew.bat test
```
