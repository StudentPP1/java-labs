package ua.kpi.comsys.dop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CancelledTest {

    @Test
    void shouldThrowExceptionIfReasonNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Cancelled(null));
    }

    @Test
    void shouldThrowExceptionIfReasonBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Cancelled("    "));
    }

    @Test
    void shouldCreate() {
        Assertions.assertDoesNotThrow(() -> new Cancelled("some reason"));
    }
}