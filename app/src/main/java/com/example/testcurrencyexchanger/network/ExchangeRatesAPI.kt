package com.example.testcurrencyexchanger.network

import com.example.testcurrencyexchanger.module.ExchangeRatesResponse
import retrofit2.Response
import retrofit2.http.GET

interface ExchangeRatesAPI {

    @GET("tasks/api/currency-exchange-rates")
    suspend fun getExchangeRates(): Response<ExchangeRatesResponse>

}