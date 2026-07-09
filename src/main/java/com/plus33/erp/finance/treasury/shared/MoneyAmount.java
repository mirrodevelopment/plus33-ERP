/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.shared
 * File              : MoneyAmount.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MoneyAmountController
 * Related Service   : MoneyAmountService, MoneyAmountServiceImpl
 * Related Repository: MoneyAmountRepository
 * Related Entity    : MoneyAmount
 * Related DTO       : N/A
 * Related Mapper    : MoneyAmountMapper
 * Related DB Table  : money_amounts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
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

    /**
     * Performs the of operation in this module.
     *
     * @param amount the amount input value
     * @param currencyCode the currencyCode input value
     * @return the MoneyAmount result
     */
    public static MoneyAmount of(BigDecimal amount, String currencyCode) {
        return new MoneyAmount(amount, currencyCode);
    }

    /**
     * Performs the of operation in this module.
     *
     * @param amount the amount input value
     * @param currencyCode the currencyCode input value
     * @return the MoneyAmount result
     */
    public static MoneyAmount of(double amount, String currencyCode) {
        return new MoneyAmount(BigDecimal.valueOf(amount), currencyCode);
    }

    /**
     * Performs the zero operation in this module.
     *
     * @param currencyCode the currencyCode input value
     * @return the MoneyAmount result
     */
    public static MoneyAmount zero(String currencyCode) {
        return new MoneyAmount(BigDecimal.ZERO, currencyCode);
    }

    // ── Arithmetic ─────────────────────────────────────────────────────────────

    /**
     * Creates a new finance and persists it to the database.
     *
     * @param other the other input value
     * @return the MoneyAmount result
     * @throws BusinessException if a business rule is violated
     */
    public MoneyAmount add(MoneyAmount other) {
        assertSameCurrency(other);
        return new MoneyAmount(this.amount.add(other.amount, DEFAULT_MC), this.currencyCode);
    }

    /**
     * Performs the subtract operation in this module.
     *
     * @param other the other input value
     * @return the MoneyAmount result
     */
    public MoneyAmount subtract(MoneyAmount other) {
        assertSameCurrency(other);
        return new MoneyAmount(this.amount.subtract(other.amount, DEFAULT_MC), this.currencyCode);
    }

    /**
     * Performs the multiply operation in this module.
     *
     * @param factor the factor input value
     * @return the MoneyAmount result
     */
    public MoneyAmount multiply(BigDecimal factor) {
        return new MoneyAmount(this.amount.multiply(factor, DEFAULT_MC), this.currencyCode);
    }

    /**
     * Performs the negate operation in this module.
     *
     * @return the MoneyAmount result
     */
    public MoneyAmount negate() {
        return new MoneyAmount(this.amount.negate(), this.currencyCode);
    }

    /**
     * Converts between Entity and DTO representations (MapStruct).
     *
     * @param exchangeRate the exchangeRate input value
     * @param targetCurrency the targetCurrency input value
     * @return the MoneyAmount result
     */
    public MoneyAmount convert(BigDecimal exchangeRate, String targetCurrency) {
        BigDecimal converted = this.amount.multiply(exchangeRate, DEFAULT_MC);
        return new MoneyAmount(converted, targetCurrency);
    }

    // ── Comparison ─────────────────────────────────────────────────────────────

    /**
     * Performs the isGreaterThan operation in this module.
     *
     * @param other the other input value
     * @return true if operation succeeded, false otherwise
     */
    public boolean isGreaterThan(MoneyAmount other) {
        assertSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    /**
     * Performs the isLessThan operation in this module.
     *
     * @param other the other input value
     * @return true if operation succeeded, false otherwise
     */
    public boolean isLessThan(MoneyAmount other) {
        assertSameCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    /**
     * Performs the isZero operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Performs the isNegative operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isNegative() {
        return this.amount.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Performs the isPositive operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    // ── Accessors ──────────────────────────────────────────────────────────────

    /**
     * Retrieves amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Retrieves currency code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
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

    /**
     * Performs the equals operation in this module.
     *
     * @param o the o input value
     * @return true if operation succeeded, false otherwise
     */
    /**
     * Performs the equals operation in this module.
     *
     * @param o the o input value
     * @return true if operation succeeded, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoneyAmount that)) return false;
        return this.amount.compareTo(that.amount) == 0
                && this.currencyCode.equals(that.currencyCode);
    }

    /**
     * Performs the hashCode operation in this module.
     *
     * @return the numeric result value
     */
    /**
     * Performs the hashCode operation in this module.
     *
     * @return the numeric result value
     */
    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currencyCode);
    }

    /**
     * Performs the toString operation in this module.
     *
     * @return the result string value
     */
    /**
     * Performs the toString operation in this module.
     *
     * @return the result string value
     */
    @Override
    public String toString() {
        return amount.toPlainString() + " " + currencyCode;
    }
}