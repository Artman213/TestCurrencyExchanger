package com.example.testcurrencyexchanger.module

import java.util.Date

class ExchangeRatesResponse(
    val base: String,
    val date: Date,
    val rates: LinkedHashMap<String, Double>
)