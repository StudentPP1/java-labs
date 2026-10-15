package ua.kpi.comsys.dop;

public record Cancelled(String reason) implements OrderStatus {
    public Cancelled {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}
