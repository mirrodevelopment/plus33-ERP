package com.plus33.erp.finance.treasury.shared;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Immutable value object representing a monetary amount with explicit currency.
 * Uses banker's rounding (HALF_EVEN) and currency-specific fractional digits for precision.
 */
public final class MoneyAmount {

    public static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
    public static final MathContext DEFAULT_MC = new MathContext(34, DEFAULT_ROUNDING);

    private final BigDecimal amount;
    private final String currencyCode;
    private final int scale;

    private MoneyAmount(BigDecimal amount, String currencyCode) {
        this.currencyCode = Objects.requireNonNull(currencyCode, "currencyCode must not be null");
        this.scale = resolveScale(currencyCode);
        this.amount = Objects.requireNonNull(amount, "amount must not be null")
                .setScale(this.scale, DEFAULT_ROUNDING);
    }

    // ── Factory Methods ────────────────────────────────────────────────────────

    public static MoneyAmount of(BigDecimal amount, String currencyCode) {
        return new MoneyAmount(amount, currencyCode);
    }

    public static MoneyAmount of(double amount, String currencyCode) {
        return new MoneyAmount(BigDecimal.valueOf(amount), currencyCode);
    }

    public static MoneyAmount zero(String currencyCode) {
        return new MoneyAmount(BigDecimal.ZERO, currencyCode);
    }

    // ── Arithmetic ─────────────────────────────────────────────────────────────

    public MoneyAmount add(MoneyAmount other) {
        assertSameCurrency(other);
        return new MoneyAmount(this.amount.add(other.amount, DEFAULT_MC), this.currencyCode);
    }

    public MoneyAmount subtract(MoneyAmount other) {
        assertSameCurrency(other);
        return new MoneyAmount(this.amount.subtract(other.amount, DEFAULT_MC), this.currencyCode);
    }

    public MoneyAmount multiply(BigDecimal factor) {
        return new MoneyAmount(this.amount.multiply(factor, DEFAULT_MC), this.currencyCode);
    }

    public MoneyAmount negate() {
        return new MoneyAmount(this.amount.negate(), this.currencyCode);
    }

    public MoneyAmount convert(BigDecimal exchangeRate, String targetCurrency) {
        BigDecimal converted = this.amount.multiply(exchangeRate, DEFAULT_MC);
        return new MoneyAmount(converted, targetCurrency);
    }

    // ── Comparison ─────────────────────────────────────────────────────────────

    public boolean isGreaterThan(MoneyAmount other) {
        assertSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(MoneyAmount other) {
        assertSameCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isNegative() {
        return this.amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    // ── Accessors ──────────────────────────────────────────────────────────────

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    // ── Internal ───────────────────────────────────────────────────────────────

    private void assertSameCurrency(MoneyAmount other) {
        if (!this.currencyCode.equals(other.currencyCode)) {
            throw new IllegalArgumentException(
                    "Currency mismatch: cannot operate on " + this.currencyCode + " and " + other.currencyCode);
        }
    }

    private static int resolveScale(String currencyCode) {
        try {
            return Currency.getInstance(currencyCode).getDefaultFractionDigits();
        } catch (IllegalArgumentException e) {
            return 2; // fallback
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoneyAmount that)) return false;
        return this.amount.compareTo(that.amount) == 0
                && this.currencyCode.equals(that.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currencyCode);
    }

    @Override
    public String toString() {
        return amount.toPlainString() + " " + currencyCode;
    }
}
