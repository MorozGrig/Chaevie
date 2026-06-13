package com.example.chaevie

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TipCalculatorTest {
    @Test
    fun calculateRoundsTipDown() {
        val result = TipCalculator.calculate(100.0, 15, 2, RoundingMode.FLOOR)

        assertEquals("15.00", result.formattedTipAmount())
        assertEquals("115.00", result.formattedTotalAmount())
        assertEquals("57.50", result.formattedAmountPerPerson())
    }

    @Test
    fun calculateRoundsTipToNearestAndFormatsWholeNumberWithTwoDecimals() {
        val result = TipCalculator.calculate(1000.0, 12, 1, RoundingMode.ROUND)

        assertEquals("120.00", result.formattedTipAmount())
        assertEquals("1120.00", result.formattedTotalAmount())
        assertEquals("1120.00", result.formattedAmountPerPerson())
    }

    @Test
    fun validatorUsesDefaultsForOptionalEmptyFields() {
        val result = TipInputValidator.validate("200.50", "", "")

        assertTrue(result is TipInputValidationResult.Success)
        result as TipInputValidationResult.Success
        assertEquals(200.50, result.input.billAmount, 0.0)
        assertEquals(15, result.input.tipPercent)
        assertEquals(1, result.input.peopleCount)
    }

    @Test
    fun validatorRejectsInvalidRangesAndMoneyPrecision() {
        assertTrue(TipInputValidator.validate("10.999", "15", "1") is TipInputValidationResult.Error)
        assertTrue(TipInputValidator.validate("10.00", "4", "1") is TipInputValidationResult.Error)
        assertTrue(TipInputValidator.validate("10.00", "15", "0") is TipInputValidationResult.Error)
    }
}
