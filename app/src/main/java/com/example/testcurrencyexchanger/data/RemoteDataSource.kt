package com.example.testcurrencyexchanger.data

import com.example.testcurrencyexchanger.module.ExchangeRatesResponse
import com.example.testcurrencyexchanger.network.ExchangeRatesAPI
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject


class RemoteDataSource @Inject constructor(retrofit: Retrofit) {
    private val exchangeRatesService: ExchangeRatesAPI =
        retrofit.create(ExchangeRatesAPI::class.java)

    suspend fun getExchangeRates(): Result<ExchangeRatesResponse> =
        exchangeRatesService.getExchangeRates().translateResult()

    private fun <T> Response<T>.translateResult() = if (isSuccessful) {
        body()!!.let {
            Result.success(it)
        }
    } else {
        val msg = errorMessage
        Result.failure(Throwable(msg))
    }

    private val <T> Response<T>.errorMessage: String
        get() = if (message().isNullOrBlank()) {
            errorBody()?.string() ?: ""
        } else message()
}