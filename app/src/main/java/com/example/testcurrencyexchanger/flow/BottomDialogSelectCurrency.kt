package com.example.testcurrencyexchanger.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testcurrencyexchanger.databinding.DialogCurrencyBinding
import com.example.testcurrencyexchanger.flow.adapter.CurrencyAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomDialogSelectCurrency(
    private val exchangeList: HashMap<String, Double>,
    private val clickInterface: ClickInterface
) : BottomSheetDialogFragment() {

    private var _binding: DialogCurrencyBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCurrencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCurrency.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCurrency.adapter = CurrencyAdapter(exchangeList, this, clickInterface)
    }
}