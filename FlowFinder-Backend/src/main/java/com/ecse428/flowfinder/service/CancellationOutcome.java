package com.ecse428.flowfinder.service.dto;

import java.math.BigDecimal;

public class CancellationOutcome {
    private final boolean fullyRefunded;
    private final BigDecimal fee;
    private final String message;

    public CancellationOutcome(boolean fullyRefunded, BigDecimal fee, String message) {
        this.fullyRefunded = fullyRefunded;
        this.fee = fee;
        this.message = message;
    }
    public boolean isFullyRefunded() { return fullyRefunded; }
    public BigDecimal getFee() { return fee; }
    public String getMessage() { return message; }
}
