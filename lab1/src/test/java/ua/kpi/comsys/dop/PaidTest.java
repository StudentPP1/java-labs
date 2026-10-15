package ua.kpi.comsys.dop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class PaidTest {

    @Test
    void shouldThrowExceptionIfPaymentNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Paid(null, BigDecimal.ONE));
    }

    @Test
    void shouldThrowExceptionIfPaymentBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Paid(" ", BigDecimal.ONE));
    }

    @Test
    void shouldThrowExceptionIfAmountLowerZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Paid("1", BigDecimal.valueOf(-1)));
    }

    @Test
    void shouldThrowExceptionIfAmountZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Paid("1", BigDecimal.ZERO));
    }

    @Test
    void shouldThrowExceptionIfAmountNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Paid("1", null));
    }

    @Test
    void shouldCreate() {
        Assertions.assertDoesNotThrow(() -> new Paid("1", BigDecimal.TEN));
    }
}