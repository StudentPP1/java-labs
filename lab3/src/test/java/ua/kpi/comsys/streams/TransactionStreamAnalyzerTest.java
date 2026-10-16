package ua.kpi.comsys.streams;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TransactionStreamAnalyzerTest {
    private static final LocalDateTime TIME = LocalDateTime.of(2026, 10, 16, 12, 0);
    private final TransactionStreamAnalyzer analyzer = new TransactionStreamAnalyzer();

    private static Transaction transaction(String id, long amount) {
        return new Transaction(id, "test", BigDecimal.valueOf(amount), TIME);
    }

    private static Stream<Transaction> infiniteStream() {
        return Stream
                .iterate(1L, i -> i + 1)
                .map(i -> transaction("t" + i, i));
    }

    private static List<String> ids(List<Transaction> transactions) {
        return transactions.stream().map(Transaction::transactionId).toList();
    }

    @Test
    void findFirstShouldThrowIfStreamNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyzer.findFirstHighValueTransaction(null, BigDecimal.TEN));
        assertEquals("stream cannot be null", exception.getMessage());
    }

    @Test
    void findFirstShouldThrowIfThresholdNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyzer.findFirstHighValueTransaction(Stream.empty(), null));
        assertEquals("threshold cannot be null", exception.getMessage());
    }

    @Test
    void findFirstShouldReturnFirstTransactionAboveThreshold() {
        Stream<Transaction> stream = Stream.of(
                transaction("a", 50),
                transaction("b", 120),
                transaction("c", 300)
        );
        Optional<Transaction> result = analyzer.findFirstHighValueTransaction(stream, BigDecimal.valueOf(100));
        assertEquals(Optional.of("b"), result.map(Transaction::transactionId));
    }

    @Test
    void findFirstShouldReturnEmptyIfNothingAboveThreshold() {
        Stream<Transaction> stream = Stream.of(transaction("a", 10), transaction("b", 20));
        assertTrue(analyzer.findFirstHighValueTransaction(stream, BigDecimal.valueOf(100)).isEmpty());
    }

    @Test
    @Timeout(2)
    void findFirstShouldWorkOnInfiniteStream() {
        Optional<Transaction> result = analyzer.findFirstHighValueTransaction(infiniteStream(), BigDecimal.valueOf(5));
        assertEquals(Optional.of("t6"), result.map(Transaction::transactionId));
    }

    @Test
    void takeUntilBudgetShouldThrowIfStreamNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyzer.takeTransactionsUntilBudgetExceeded(null, BigDecimal.TEN));
        assertEquals("stream cannot be null", exception.getMessage());
    }

    @Test
    void takeUntilBudgetShouldThrowIfMaxBudgetNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyzer.takeTransactionsUntilBudgetExceeded(Stream.empty(), null));
        assertEquals("maxBudget cannot be null", exception.getMessage());
    }

    @Test
    void takeUntilBudgetShouldStopBeforeBudgetExceeded() {
        Stream<Transaction> stream = Stream.of(
                transaction("a", 30),
                transaction("b", 40),
                transaction("c", 50)
        );
        List<Transaction> result = analyzer.takeTransactionsUntilBudgetExceeded(stream, BigDecimal.valueOf(100));
        assertEquals(List.of("a", "b"), ids(result));
    }

    @Test
    void takeUntilBudgetShouldIncludeTransactionReachingBudgetExactly() {
        Stream<Transaction> stream = Stream.of(
                transaction("a", 60),
                transaction("b", 40),
                transaction("c", 1)
        );
        List<Transaction> result = analyzer.takeTransactionsUntilBudgetExceeded(stream, BigDecimal.valueOf(100));
        assertEquals(List.of("a", "b"), ids(result));
    }

    @Test
    @Timeout(2)
    void takeUntilBudgetShouldStopOnInfiniteStream() {
        List<Transaction> result = analyzer.takeTransactionsUntilBudgetExceeded(infiniteStream(), BigDecimal.TEN);
        assertEquals(List.of("t1", "t2", "t3", "t4"), ids(result));
    }

    @Test
    void processBatchesShouldThrowIfStreamNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyzer.processBatches(null, 2, 2));
        assertEquals("stream cannot be null", exception.getMessage());
    }

    @Test
    void processBatchesShouldThrowIfBatchSizeZero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyzer.processBatches(infiniteStream(), 0, 2));
        assertEquals("batchSize must be greater than 0", exception.getMessage());
    }

    @Test
    void processBatchesShouldThrowIfMaxBatchesZero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyzer.processBatches(infiniteStream(), 2, 0));
        assertEquals("maxBatches must be greater than 0", exception.getMessage());
    }

    @Test
    @Timeout(2)
    void processBatchesShouldReturnExactlyMaxBatchesFromInfiniteStream() {
        List<List<Transaction>> result = analyzer.processBatches(infiniteStream(), 3, 2);
        assertEquals(List.of(List.of("t1", "t2", "t3"), List.of("t4", "t5", "t6")),
                result.stream().map(TransactionStreamAnalyzerTest::ids).toList());
    }

    @Test
    @Timeout(2)
    void processBatchesShouldReturnFullBatchesFromGeneratedInfiniteStream() {
        Stream<Transaction> stream = Stream.generate(() -> transaction("x", 1));
        List<List<Transaction>> result = analyzer.processBatches(stream, 4, 3);
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(batch -> batch.size() == 4));
    }
}
