package ua.kpi.comsys.dop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ShippedTest {

    @Test
    void shouldThrowExceptionIfCodeNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Shipped(null, LocalDate.now()));
    }

    @Test
    void shouldThrowExceptionIfCodeBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Shipped(" ", LocalDate.now()));
    }

    @Test
    void shouldThrowExceptionIfDateNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Shipped("1", null));
    }

    @Test
    void shouldCreate() {
        Assertions.assertDoesNotThrow(() -> new Shipped("1", LocalDate.now()));
    }
}