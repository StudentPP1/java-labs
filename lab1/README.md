# Лабораторна робота №1

## Опис проєкту
Предметна модель системи управління замовленнями (варіант 0), побудована за принципами
Data-Oriented Programming:
- статуси замовлення описані незмінними `record` в ієрархії `sealed interface OrderStatus`
  (`Pending`, `Paid`, `Shipped`, `Cancelled`), валідація даних - у компактних конструкторах;
- бізнес-логіка винесена в окремий клас `OrderProcessor`, який через `switch` expression
  з pattern matching (без `default`) формує текстовий опис статусу за допомогою Text Blocks.

Усі класи знаходяться в пакеті `ua.kpi.comsys.dop`.

## Вимоги
- JDK 25 
- Maven 3.9+

## Запуск тестів
```bash
mvn test
```
Тести (JUnit 5) знаходяться в `src/test/java/ua/kpi/comsys/dop` і покривають усі гілки
`switch` в `OrderProcessor`, а також валідацію в конструкторах записів.

## Завдання

## Визначення варіанта
V = N mod 3 = 15 mod 3 = 0

## Система управління замовленнями
Ієрархія даних
Ієрархію даних реалізувати за допомогою sealed interface та record.
Усі інтерфейси та записи (records), які описують ієрархію даних мають 
бути публічними та знаходитися в пакеті package ua.kpi.comsys.dop;
```
+ OrderStatus
  |--- Pending
  |--- Paid (String paymentID, java.math.BigDecimal amount)
  |--- Shipped (String trackingCode, java.time.LocalDate dispatchDate)
  |--- Cancelled(String reason)
```
Валідація в конструкторах record:
У Paid: якщо paymentId == null або paymentId.isBlank(), або amount <= 0, метод повинен кидати IllegalArgumentException.
У Shipped: якщо trackingCode == null або trackingCode.isBlank(), або dispatchDate == null, кидати IllegalArgumentException.
У Cancelled: якщо reason == null або reason.isBlank(), кидати IllegalArgumentException.
Клас-обробник та детальна логіка трансформації
Сигнатура:
```java
package ua.kpi.comsys.dop;

public class OrderProcessor {
    public String process(OrderStatus status) {  }
}
```

Поведінка та логіка обробника:
Якщо status == null, метод зобов'язаний кидати IllegalArgumentException("Status cannot be null").
Обробка реалізується виключно через switch expression з pattern matching, що покриває всі варіанти без default.
Повертає стрічку, сформовану за допомогою Text Blocks ("""), суворо за шаблонами:

+ Для Pending:
```
    Order Status: PENDING
    Details: Order is currently pending processing.
```
+ Для Paid:
```
    Order Status: PAID
    Details: Payment ID: _, Amount: $_
```
+ Для Shipped:
```
    Order Status: SHIPPED
    Details: Tracking Code: _, Dispatch Date: _
```
+ Для Cancelled:
```
    Order Status: CANCELLED
    Details: Reason: _
```
