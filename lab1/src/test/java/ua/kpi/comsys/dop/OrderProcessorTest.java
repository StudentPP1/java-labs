package ua.kpi.comsys.dop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

class OrderProcessorTest {
    private static final OrderProcessor processor = new OrderProcessor();

    @Test
    void shouldThrowExceptionIfStatusNull() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> processor.process(null));
        Assertions.assertEquals("Status cannot be null", exception.getMessage());
    }

    @Test
    void shouldReturnPending() {
        Assertions.assertEquals("Order Status: PENDING\nDetails: Order is currently pending processing.",
                processor.process(new Pending()));
    }

    @Test
    void shouldReturnPaid() {
        Assertions.assertEquals("Order Status: PAID\nDetails: Payment ID: 123, Amount: $123",
                processor.process(new Paid("123", new BigDecimal("123"))));
    }

    @Test
    void shouldReturnShipped() {
        Assertions.assertEquals("Order Status: SHIPPED\nDetails: Tracking Code: 123, Dispatch Date: 2026-10-15",
                processor.process(new Shipped("123", LocalDate.of(2026, 10, 15))));
    }

    @Test
    void shouldReturnCancelled() {
        Assertions.assertEquals("Order Status: CANCELLED\nDetails: Reason: Out of stock",
                processor.process(new Cancelled("Out of stock")));
    }
}
