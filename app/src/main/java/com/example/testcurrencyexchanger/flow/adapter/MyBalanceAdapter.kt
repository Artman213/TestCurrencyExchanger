package com.example.testcurrencyexchanger.flow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testcurrencyexchanger.databinding.ItemMyBalanceBinding
import com.example.testcurrencyexchanger.util.stringFormat2

class MyBalanceAdapter(private val myBalance: HashMap<String, Double>) : RecyclerView
.Adapter<MyBalanceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyBalanceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(myBalance.keys.elementAt(position), myBalance.values.elementAt(position))

    }

    class ViewHolder(
        private val binding: ItemMyBalanceBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String, cash: Double) {
            binding.tvMoney.text = cash.stringFormat2()
            binding.tvName.text = name
        }
    }


    override fun getItemCount() = myBalance.size
}