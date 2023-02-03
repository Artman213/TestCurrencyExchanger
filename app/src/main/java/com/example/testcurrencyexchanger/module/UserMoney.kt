package com.example.testcurrencyexchanger.module

import com.example.testcurrencyexchanger.util.multiply

class UserMoney {

    var userMoneyHashMap: HashMap<String, Double> = HashMap()

    fun getSellWithReturnFee(
        sell: String,
        receive: String,
        cash: Double,
        rate: Double,
        fee: Double
    ): Double? {
        var money = userMoneyHashMap[sell] ?: return null
        money.let {
            money -= (cash + cash.multiply(fee))
        }
        if (money >= 0)
            getReceive(receive, cash, rate)
        else {
            return null
        }
        userMoneyHashMap[sell] = money
        return cash.multiply(fee)
    }

    private fun getReceive(key: String, cash: Double, rate: Double) {
        var money = userMoneyHashMap[key] ?: return
        money.let {
            money =+ (money + cash.multiply(rate))
        }
        userMoneyHashMap[key] = money
    }

}