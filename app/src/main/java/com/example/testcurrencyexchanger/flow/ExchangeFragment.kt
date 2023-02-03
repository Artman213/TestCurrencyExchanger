package com.example.testcurrencyexchanger.flow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.testcurrencyexchanger.databinding.FragmentExchangeBinding

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ExchangeFragment : Fragment(), ClickInterface {

    private var _binding: FragmentExchangeBinding? = null

    private val binding get() = _binding!!

    private val viewModel: ExchangeFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExchangeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.exchangeRates.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                result.onSuccess {

                    viewModel.initBalance(it.rates, binding.rvBalanceList, requireContext())
                }
            } else {
                result.onFailure {
                    Toast.makeText(
                        requireContext(),
                        it.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

        }
        viewModel.initRecycleView(binding.rvBalanceList, requireContext())
        viewModel.click(binding, requireContext())
        viewModel.addSellTextChange(binding)
        viewModel.addReceiveTextChange(binding)
        viewModel.clickSelect(binding.llSelect, requireActivity().supportFragmentManager, this)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun click(currency: String) {
        binding.tvReceiveCurrency.text = currency
        viewModel.recalculation(binding)
    }
}