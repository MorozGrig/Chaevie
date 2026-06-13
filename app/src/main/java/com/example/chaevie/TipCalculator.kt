package com.example.chaevie

import java.util.Locale
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

enum class RoundingMode {
    FLOOR,
    CEIL,
    ROUND
}

data class TipCalculationResult(
    val tipAmount: Double,
    val totalAmount: Double,
    val amountPerPerson: Double
) {
    fun formattedTipAmount(): String = tipAmount.formatAsMoney()

    fun formattedTotalAmount(): String = totalAmount.formatAsMoney()

    fun formattedAmountPerPerson(): String = amountPerPerson.formatAsMoney()
}

object TipCalculator {
    fun calculate(
        billAmount: Double,
        tipPercent: Int,
        peopleCount: Int,
        roundingMode: RoundingMode
    ): TipCalculationResult {
        val rawTipAmount = billAmount * tipPercent / 100
        val roundedTipAmount = when (roundingMode) {
            RoundingMode.FLOOR -> floor(rawTipAmount)
            RoundingMode.CEIL -> ceil(rawTipAmount)
            RoundingMode.ROUND -> round(rawTipAmount)
        }
        val totalAmount = billAmount + roundedTipAmount

        return TipCalculationResult(
            tipAmount = roundedTipAmount,
            totalAmount = totalAmount,
            amountPerPerson = totalAmount / peopleCount
        )
    }
}

private fun Double.formatAsMoney(): String = String.format(Locale.US, "%.2f", this)
