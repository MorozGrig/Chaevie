package com.example.chaevie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.chaevie.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calculateButton.setOnClickListener {
            calculateTip()
        }
    }

    private fun calculateTip() {
        when (val validationResult = validateInput()) {
            is TipInputValidationResult.Success -> showResult(validationResult.input)
            is TipInputValidationResult.Error -> showError(validationResult.message)
        }
    }

    private fun validateInput(): TipInputValidationResult = TipInputValidator.validate(
        billAmountText = binding.billAmountEditText.text.toString(),
        tipPercentText = binding.tipPercentEditText.text.toString(),
        peopleCountText = binding.peopleCountEditText.text.toString()
    )

    private fun showResult(input: TipInput) {
        val result = TipCalculator.calculate(
            billAmount = input.billAmount,
            tipPercent = input.tipPercent,
            peopleCount = input.peopleCount,
            roundingMode = selectedRoundingMode()
        )

        binding.errorTextView.isVisible = false
        binding.resultsGroup.isVisible = true
        binding.tipAmountTextView.text = getString(R.string.tip_amount_result, result.formattedTipAmount())
        binding.totalAmountTextView.text = getString(R.string.total_amount_result, result.formattedTotalAmount())
        binding.amountPerPersonTextView.text = getString(
            R.string.amount_per_person_result,
            result.formattedAmountPerPerson()
        )
    }

    private fun selectedRoundingMode(): RoundingMode = when (binding.roundingRadioGroup.checkedRadioButtonId) {
        R.id.rounding_ceil_radio_button -> RoundingMode.CEIL
        R.id.rounding_round_radio_button -> RoundingMode.ROUND
        else -> RoundingMode.FLOOR
    }

    private fun showError(message: String) {
        binding.resultsGroup.isVisible = false
        binding.errorTextView.text = message
        binding.errorTextView.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
