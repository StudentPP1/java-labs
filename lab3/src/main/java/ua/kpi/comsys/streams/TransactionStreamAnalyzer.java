package ua.kpi.comsys.streams;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class TransactionStreamAnalyzer {

    // Знайти першу транзакцію, що перевищує заданий ліміт
    public Optional<Transaction> findFirstHighValueTransaction(Stream<Transaction> stream, BigDecimal threshold) {
        return stream.filter(t -> t.amount().compareTo(threshold) > 0).findFirst();
    }

    // Вибрати транзакції зі стріму, поки їхня сума
    // не перевищує ліміт
    public List<Transaction> takeTransactionsUntilBudgetExceeded(Stream<Transaction> stream, BigDecimal maxBudget) {
        AtomicReference<BigDecimal> currentBudget = new AtomicReference<>(BigDecimal.ZERO);
        return stream.takeWhile(transaction -> {
            currentBudget.updateAndGet(v -> v.add(transaction.amount()));
            return currentBudget.get().compareTo(maxBudget) <= 0;
        }).toList();
    }

    // Кастомний Gatherer для розбиття нескінченного стріма
    // на фіксовані вікна (батчі) розміром batchSize
    // з обмеженням кількості батчів
    public List<List<Transaction>> processBatches(Stream<Transaction> stream, int batchSize, int maxBatches) {
        return stream.gather(fixedWindow(batchSize)).limit(maxBatches).toList();
    }

    private static <T> Gatherer<T, ?, List<T>> fixedWindow(int size) {
        return Gatherer.ofSequential(
                () -> new ArrayList<T>(size),
                // for each element
                Gatherer.Integrator.ofGreedy((currentBatch, element, totalList) -> {
                    currentBatch.add(element);
                    if (currentBatch.size() < size) {
                        return true;
                    }
                    List<T> full = List.copyOf(currentBatch);
                    currentBatch.clear();
                    return totalList.push(full);
                }),
                // after last element of stream
                (currentBatch, totalList) -> {
                    if (!currentBatch.isEmpty()) {
                        totalList.push(List.copyOf(currentBatch));
                    }
                }
        );
    }
}
