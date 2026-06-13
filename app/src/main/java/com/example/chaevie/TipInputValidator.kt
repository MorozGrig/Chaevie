package com.example.chaevie

data class TipInput(
    val billAmount: Double,
    val tipPercent: Int,
    val peopleCount: Int
)

sealed class TipInputValidationResult {
    data class Success(val input: TipInput) : TipInputValidationResult()
    data class Error(val message: String) : TipInputValidationResult()
}

object TipInputValidator {
    private const val DEFAULT_TIP_PERCENT = 15
    private const val DEFAULT_PEOPLE_COUNT = 1
    private val moneyPattern = Regex("^\\d+(\\.\\d{1,2})?$")
    private val integerPattern = Regex("^\\d+$")

    fun validate(
        billAmountText: String,
        tipPercentText: String,
        peopleCountText: String
    ): TipInputValidationResult {
        val trimmedBillAmount = billAmountText.trim()
        if (trimmedBillAmount.isEmpty()) {
            return TipInputValidationResult.Error("Введите сумму счёта.")
        }
        if (!moneyPattern.matches(trimmedBillAmount)) {
            return TipInputValidationResult.Error("Сумма счёта должна быть положительным числом с точностью до 2 знаков.")
        }

        val billAmount = trimmedBillAmount.toDoubleOrNull()
            ?: return TipInputValidationResult.Error("Сумма счёта должна быть числом.")
        if (billAmount <= 0.0) {
            return TipInputValidationResult.Error("Сумма счёта должна быть больше 0.")
        }

        val tipPercent = parseOptionalInteger(
            value = tipPercentText,
            defaultValue = DEFAULT_TIP_PERCENT
        ) ?: return TipInputValidationResult.Error("Процент чаевых должен быть целым числом от 5 до 30.")
        if (tipPercent !in 5..30) {
            return TipInputValidationResult.Error("Процент чаевых должен быть от 5 до 30.")
        }

        val peopleCount = parseOptionalInteger(
            value = peopleCountText,
            defaultValue = DEFAULT_PEOPLE_COUNT
        ) ?: return TipInputValidationResult.Error("Количество человек должно быть целым числом от 1 до 20.")
        if (peopleCount !in 1..20) {
            return TipInputValidationResult.Error("Количество человек должно быть от 1 до 20.")
        }

        return TipInputValidationResult.Success(
            TipInput(
                billAmount = billAmount,
                tipPercent = tipPercent,
                peopleCount = peopleCount
            )
        )
    }

    private fun parseOptionalInteger(
        value: String,
        defaultValue: Int
    ): Int? {
        val trimmedValue = value.trim()
        if (trimmedValue.isEmpty()) {
            return defaultValue
        }
        if (!integerPattern.matches(trimmedValue)) {
            return null
        }
        return trimmedValue.toIntOrNull()
    }
}
