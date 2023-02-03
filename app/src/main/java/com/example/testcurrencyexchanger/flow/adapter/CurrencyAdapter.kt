package com.example.testcurrencyexchanger.flow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testcurrencyexchanger.databinding.ItemCurrencyBinding
import com.example.testcurrencyexchanger.flow.BottomDialogSelectCurrency
import com.example.testcurrencyexchanger.flow.ClickInterface
import com.example.testcurrencyexchanger.util.stringFormat2

class CurrencyAdapter(
    private val exchangeList: HashMap<String, Double>,
    private val bottomDialogSelectCurrency: BottomDialogSelectCurrency,
    private val clickInterface: ClickInterface
) : RecyclerView
.Adapter<CurrencyAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCurrencyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            clickInterface.click(exchangeList.keys.elementAt(position))
            bottomDialogSelectCurrency.dismiss()
        }
        holder.bind(
            exchangeList.keys.elementAt(position),
            exchangeList.values.elementAt(position)
        )


    }

    class ViewHolder(
        private val binding: ItemCurrencyBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String, currency: Double) {
            binding.tvCurrency.text = currency.stringFormat2()
            binding.tvName.text = name
            adapterPosition
        }
    }


    override fun getItemCount() = exchangeList.size
}