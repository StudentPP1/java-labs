# Лабораторна робота №3

## Опис проєкту
Аналіз потоку фінансових транзакцій за допомогою Stream API та кастомного `Gatherer`.

Клас `ua.kpi.comsys.streams.TransactionStreamAnalyzer`:
- `findFirstHighValueTransaction(stream, threshold)` - перша транзакція, сума якої перевищує `threshold`,
  або `Optional.empty()`, якщо такої немає. Працює і на нескінченних стрімах;
- `takeTransactionsUntilBudgetExceeded(stream, maxBudget)` - транзакції з початку стріму,
  поки їхня сумарна сума не перевищить `maxBudget`;
- `processBatches(stream, batchSize, maxBatches)` - групує стрім у пакети по `batchSize` елементів
  за допомогою кастомного `Gatherer` (`Gatherer.ofSequential`) і зупиняється після `maxBatches` пакетів.

Якщо стрім або обов'язковий аргумент дорівнює `null`, а також якщо `batchSize <= 0` чи `maxBatches <= 0`,
кидається `IllegalArgumentException`.

Проєкт збирається Gradle; залежності (JUnit 5) керуються через Version Catalog `gradle/libs.versions.toml`.

Тести перевіряють методи як на скінченних, так і на нескінченних стрімах (`Stream.iterate`, `Stream.generate`);
тести з нескінченними стрімами мають `@Timeout(2)`, що підтверджує відсутність зависань.

## Визначення варіанта
V = N mod 3 = 15 mod 3 = 0 

## Вимоги
- JDK 25 (використовується `Gatherer`). Якщо JDK 25 не встановлено, Gradle завантажить його автоматично.

## Збірка та запуск тестів
```bash
./gradlew build
./gradlew test
```
На Windows: `gradlew.bat build` / `gradlew.bat test`.
