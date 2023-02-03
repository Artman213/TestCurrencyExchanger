package com.example.testcurrencyexchanger.flow

import android.content.Context
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testcurrencyexchanger.R
import com.example.testcurrencyexchanger.flow.adapter.MyBalanceAdapter
import com.example.testcurrencyexchanger.data.Repository
import com.example.testcurrencyexchanger.data.SecureRepository
import com.example.testcurrencyexchanger.databinding.FragmentExchangeBinding
import com.example.testcurrencyexchanger.module.ExchangeRatesResponse
import com.example.testcurrencyexchanger.util.Constants
import com.example.testcurrencyexchanger.util.multiply
import com.example.testcurrencyexchanger.util.sortByBalance
import com.example.testcurrencyexchanger.util.stringFormat2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeFragmentViewModel @Inject constructor(
    private val repository: Repository,
    private val secureRepository: SecureRepository
) : ViewModel() {
    private val _exchangeRates = MutableLiveData<Result<ExchangeRatesResponse>>()
    val exchangeRates: LiveData<Result<ExchangeRatesResponse>> =
        _exchangeRates.distinctUntilChanged()
    var exchangeList: HashMap<String, Double> = HashMap()

    init {
        getExchangeRates()
    }

    fun click(binding: FragmentExchangeBinding, context: Context) {
        binding.buttonSubmit.setOnClickListener {
            var fee: Double? = null
            val userMoney = secureRepository.getUserMoney()
            if (binding.etSell.text.toString().isNotEmpty()) {
                if (!secureRepository.checkFee()) {
                    userMoney?.let {
                        exchangeList[binding.tvReceiveCurrency.text.toString()]?.let { rate ->
                            fee = it.getSellWithReturnFee(
                                binding.tvSellCurrency.text.toString(),
                                binding.tvReceiveCurrency.text.toString(),
                                binding.etSell.text.toString().toDouble(),
                                rate,
                                Constants.ZERO_FEE
                            )
                        }
                    }
                } else {
                    userMoney?.let {
                        exchangeList[binding.tvReceiveCurrency.text.toString()]?.let { rate ->
                            fee = it.getSellWithReturnFee(
                                binding.tvSellCurrency.text.toString(),
                                binding.tvReceiveCurrency.text.toString(),
                                binding.etSell.text.toString().toDouble(),
                                rate,
                                Constants.FEE
                            )
                        }
                    }
                }
                if (fee != null) {
                    userMoney?.let {
                        secureRepository.updateUserMoney(it)
                        initRecycleView(binding.rvBalanceList, context)
                        Constants.dialogString(
                            context, context.resources
                                .getString(
                                    R.string.success_convert,
                                    binding.etSell.text.toString(),
                                    binding.tvSellCurrency.text.toString(),
                                    binding.etReceive.text.toString(),
                                    binding.tvReceiveCurrency.text.toString(),
                                    fee.toString(),
                                    binding.tvSellCurrency.text.toString()
                                )
                        )
                    }

                } else {
                    Constants.dialogString(
                        context,
                        context.resources.getString(R.string.failure_convert)
                    )
                }
            }


        }

    }

    fun addSellTextChange(binding: FragmentExchangeBinding) {
        binding.etSell.addTextChangedListener {
            it?.let {
                if (binding.etSell.isFocused) {

                    if (it.toString().isNotEmpty()) {
                        exchangeList[binding.tvReceiveCurrency.text]?.let { rate ->
                            binding.etReceive.setText(
                                it.toString().toDouble().multiply(rate).stringFormat2()
                            )
                        }
                    } else {
                        binding.etReceive.setText("")
                    }
                }
            }
        }
    }

    fun addReceiveTextChange(binding: FragmentExchangeBinding) {
        binding.etReceive.addTextChangedListener {
            it?.let {
                if (binding.etReceive.isFocused) {
                    if (it.toString().isNotEmpty()) {

                        exchangeList[binding.tvReceiveCurrency.text]?.let { rate ->
                            binding.etSell.setText(
                                it.toString().toDouble().div(rate).stringFormat2()
                            )
                        }
                    } else {
                        binding.etSell.setText("")
                    }
                }
            }
        }
    }

    fun clickSelect(
        selectLayout: LinearLayout,
        fragmentManager: FragmentManager,
        clickInterface: ClickInterface
    ) {
        selectLayout.setOnClickListener {
            BottomDialogSelectCurrency(exchangeList, clickInterface).show(fragmentManager, "")
        }
    }

    fun initBalance(
        exchangeList: HashMap<String, Double>, recyclerView: RecyclerView,
        context: Context
    ) {
        this.exchangeList = exchangeList
        if (recyclerView.adapter == null)
            initRecycleView(recyclerView, context)
    }

    fun initRecycleView(recyclerView: RecyclerView, context: Context) {
        recyclerView.layoutManager = LinearLayoutManager(
            context, RecyclerView.HORIZONTAL,
            false
        )
        secureRepository.getUserMoney()?.let {
            recyclerView.adapter = MyBalanceAdapter(it.userMoneyHashMap.sortByBalance())
        }
    }


    private fun getExchangeRates() {
        viewModelScope.launch(Dispatchers.IO) {
            repeat(Int.MAX_VALUE) {
                repository.getExchangeRates().flowOn(Dispatchers.IO).collect {
                    it.let {
                        _exchangeRates.postValue(it)
                    }
                    delay(5000)
                }
            }
        }
    }

    fun recalculation(binding: FragmentExchangeBinding){
        if (binding.etSell.text.toString().isNotEmpty()){
            exchangeList[binding.tvReceiveCurrency.text]?.let { rate ->
                binding.etReceive.setText(
                    binding.etSell.text.toString().toDouble().multiply(rate).stringFormat2()
                )
            }
        }
    }


}