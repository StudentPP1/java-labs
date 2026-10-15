package ua.kpi.comsys.dop;

import java.math.BigDecimal;

public record Paid(String paymentId, BigDecimal amount) implements OrderStatus {
    public Paid {
        if (paymentId == null || paymentId.isBlank() || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
